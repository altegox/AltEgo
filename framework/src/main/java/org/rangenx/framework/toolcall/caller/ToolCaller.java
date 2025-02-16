package org.rangenx.framework.toolcall.caller;

import org.rangenx.common.enums.CacheEnum;
import org.rangenx.common.exception.RangenException;
import org.rangenx.common.exception.ToolNotFindException;
import org.rangenx.framework.config.RangenConfig;
import org.rangenx.framework.ioc.InstanceUtils;
import org.rangenx.framework.toolcall.ToolEntity;
import org.rangenx.framework.toolcall.ToolCacheManager;
import org.rangenx.framework.toolcall.ToolManager;
import org.rangenx.framework.toolcall.TypeReference;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import static org.rangenx.framework.toolcall.ToolCacheManager.getCacheKey;

public class ToolCaller<T> implements Caller<T> {

    private final ToolManager toolManager = ToolManager.getInstance();
    private final ToolCacheManager toolCacheManager = ToolCacheManager.getInstance();
    private final Type returnType;

    public ToolCaller() {
        this.returnType = null;
    }

    public ToolCaller(TypeReference<T> typeReference) {
        this.returnType = typeReference.getType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(String toolName, Object... args) {
        ToolEntity tool = toolManager.getTool(toolName, args);
        if (tool == null) {
            throw new ToolNotFindException("Tool not found: " + toolName + ", args: " + Arrays.toString(args));
        }

        Object cacheValue = getFromCache(tool, args);
        if (cacheValue != null) return (T) cacheValue;

        try {
            Method resolvedMethod = resolveMethod(tool, args);

            Object instance = InstanceUtils.createInstance(resolvedMethod.getDeclaringClass());
            Object result = resolvedMethod.invoke(instance, args);

            cacheResult(result, tool, args, resolvedMethod);

            return validateReturnType(result);
        } catch (Exception e) {
            handleInvocationException(e, tool);
        }
        return null;
    }

    private Object getFromCache(ToolEntity tool, Object... args) {
        if (RangenConfig.isEnableCallCache() && toolCacheManager.isCachedMethod(tool.signature())) {
            Object cacheValue = toolCacheManager.getCachedTool(getCacheKey(tool, args));
            if (cacheValue == CacheEnum.NULL) return null;
            return cacheValue;
        }
        return null;
    }

    private Method resolveMethod(ToolEntity tool, Object... args) throws NoSuchMethodException {
        Class<?> declaringClass = tool.method().getDeclaringClass();
        Method[] methods = declaringClass.getMethods();

        for (Method method : methods) {
            if (!method.getName().equals(tool.method().getName())) continue;
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != args.length) continue;
            boolean matched = true;
            for (int i = 0; i < paramTypes.length; i++) {
                if (!isAssignable(args[i], paramTypes[i])) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return method;
            }
        }
        throw new NoSuchMethodException("No matching method found: " + tool.method().getName()
                + " in class " + declaringClass.getName());
    }

    // 检查参数类型是否匹配
    private boolean isAssignable(Object arg, Class<?> paramType) {
        if (arg == null) {
            return !paramType.isPrimitive();
        }
        Class<?> argClass = arg.getClass();
        if (paramType.isPrimitive()) {
            return getWrapperClass(paramType).isAssignableFrom(argClass);
        } else {
            return paramType.isAssignableFrom(argClass);
        }
    }

    private Class<?> getWrapperClass(Class<?> primitiveType) {
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == double.class) return Double.class;

        return primitiveType;
    }

    private void cacheResult(Object result, ToolEntity toolEntity, Object[] args, Method resolvedMethod) {
        if (RangenConfig.isEnableCallCache() && toolCacheManager.isCachedMethod(toolEntity.signature())) {
            toolCacheManager.addCacheTool(
                    getCacheKey(
                            ToolEntity.of(resolvedMethod, null, null), args
                    ), Objects.requireNonNullElse(result, CacheEnum.NULL));
        }
    }

    @SuppressWarnings("unchecked")
    private T validateReturnType(Object result) {
        if (result != null && returnType != null && !((Class<?>) returnType).isInstance(result)) {
            throw new ClassCastException("Return type mismatch, expected: " + returnType + ", but got: "
                    + result.getClass());
        }
        return (T) result;
    }

    private void handleInvocationException(Exception e, ToolEntity toolEntity) {
        if (e instanceof NoSuchMethodException) {
            throw new RangenException("No matching method found: " + toolEntity.signature(), e);
        } else {
            throw new RangenException("Error invoking tool: " + toolEntity.toolName(), e);
        }
    }

}
