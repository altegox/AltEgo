package org.rangenx.toolcall.caller;

import org.rangenx.common.enums.CacheEnum;
import org.rangenx.common.exception.RangenException;
import org.rangenx.common.exception.ToolNotFindException;
import org.rangenx.toolcall.ToolCacheManager;
import org.rangenx.toolcall.ToolContainer;
import org.rangenx.toolcall.TypeReference;
import org.rangenx.toolcall.ToolConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

import static org.rangenx.toolcall.ToolCacheManager.getCacheKey;

public class DefaultCaller<T> implements Caller<T> {

    private final ToolContainer toolContainer = ToolContainer.getInstance();
    private final ToolCacheManager toolCache = ToolCacheManager.getInstance();
    private final Type returnType;

    public DefaultCaller() {
        this.returnType = null;
    }

    public DefaultCaller(TypeReference<T> typeReference) {
        this.returnType = typeReference.getType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(String toolName, Object... args) {
        Method method = toolContainer.getTool(toolName);
        if (method == null) {
            throw new ToolNotFindException("Tool not found: " + toolName);
        }

        boolean isCacheMethod = toolCache.isCachedMethod(toolName);
        if (ToolConfig.isEnableToolCache() && isCacheMethod) {
            Object cacheValue = toolCache.getCachedTool(getCacheKey(method, args));
            if (cacheValue != null) {
                if (cacheValue == CacheEnum.NULL) return null;
                return (T) cacheValue;
            }
        }

        try {
            Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
            Object result = method.invoke(instance, args);
            if (ToolConfig.isEnableToolCache() && isCacheMethod) {
                toolCache.addCacheTool(getCacheKey(method, args), Objects.requireNonNullElse(result, CacheEnum.NULL));
            }
            if (result != null && returnType != null && !((Class<?>) returnType).isInstance(result)) {
                throw new ClassCastException("Return type mismatch, expected: " + returnType + ", now: " + result.getClass());
            }
            return (T) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RangenException("Error invoke tool: " + toolName, e);
        }
    }

}

