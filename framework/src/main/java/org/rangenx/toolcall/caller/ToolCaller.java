package org.rangenx.toolcall.caller;

import org.rangenx.common.enums.CacheEnum;
import org.rangenx.common.exception.RangenException;
import org.rangenx.common.exception.ToolNotFindException;
import org.rangenx.config.RangenConfig;
import org.rangenx.toolcall.ToolEntity;
import org.rangenx.toolcall.ToolCacheManager;
import org.rangenx.toolcall.ToolManager;
import org.rangenx.toolcall.TypeReference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import static org.rangenx.toolcall.ToolCacheManager.getCacheKey;

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
        ToolEntity toolEntity = toolManager.getTool(toolName, args);
        if (toolEntity == null) {
            throw new ToolNotFindException("Tool not found: " + toolName + ", args: " + Arrays.toString(args));
        }

        boolean isCacheMethod = toolCacheManager.isCachedMethod(toolEntity.signature());
        if (RangenConfig.isEnableCallCache() && isCacheMethod) {
            Object cacheValue = toolCacheManager.getCachedTool(getCacheKey(toolEntity, args));
            if (cacheValue != null) {
                /*
                 * 用于区分 '未缓存' 与 '缓存值为 null' 两种情况
                 * - 如果是 CacheEnum.NULL，则说明已命中缓存，但缓存值为 null
                 * - 如果是 null，则说明未命中缓存，直接返回 null
                 */
                if (cacheValue == CacheEnum.NULL) return null;
                return (T) cacheValue;
            }
        }

        try {
            // 根据参数动态解析重载方法
            Class<?> declaringClass = toolEntity.method().getDeclaringClass();
            Class<?>[] argTypes = Arrays.stream(args)
                    .map(arg -> {
                        if (arg == null) return Object.class;
                        Class<?> clazz = arg.getClass();
                        if (List.class.isAssignableFrom(clazz)) return List.class;
                        if (Map.class.isAssignableFrom(clazz)) return Map.class;
                        if (Set.class.isAssignableFrom(clazz)) return Set.class;
                        if (Collection.class.isAssignableFrom(clazz)) return Collection.class;
                        if (clazz == Integer.class) return int.class;
                        if (clazz == Double.class) return double.class;
                        if (clazz == Long.class) return long.class;
                        if (clazz == Boolean.class) return boolean.class;
                        if (clazz == Float.class) return float.class;
                        if (clazz == Character.class) return char.class;
                        if (clazz == Byte.class) return byte.class;
                        if (clazz == Short.class) return short.class;
                        if (clazz == String.class) return String.class;
                        if (clazz.isArray()) return clazz.getComponentType();
                        if (clazz.isEnum()) return Enum.class;
                        return clazz;
                    }).toArray(Class<?>[]::new);

            Method resolvedMethod = declaringClass.getDeclaredMethod(toolEntity.method().getName(), argTypes);
            resolvedMethod.setAccessible(true);

            Object instance = declaringClass.getDeclaredConstructor().newInstance();
            Object result = resolvedMethod.invoke(instance, args);

            if (RangenConfig.isEnableCallCache() && isCacheMethod) {
                toolCacheManager.addCacheTool(getCacheKey(ToolEntity.of(resolvedMethod, null, null), args),
                        Objects.requireNonNullElse(result, CacheEnum.NULL));
            }

            // 返回类型检查
            if (result != null && returnType != null && !((Class<?>) returnType).isInstance(result)) {
                throw new ClassCastException("Return type mismatch, expected: " + returnType + ", but got: " + result.getClass());
            }

            return (T) result;
        } catch (NoSuchMethodException e) {
            throw new RangenException("No matching method found: " + toolEntity.signature(), e);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RangenException("Error invoking tool: " + toolName, e);
        }
    }

}

