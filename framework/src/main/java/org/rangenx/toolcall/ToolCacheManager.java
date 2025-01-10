package org.rangenx.toolcall;

import org.rangenx.annotation.AnnotationProcessor;
import org.rangenx.annotation.ToolCache;

import java.lang.reflect.Method;
import java.util.*;

public class ToolCacheManager {

    private static volatile ToolCacheManager INSTANCE = null;

    private static final Map<String, Object> toolCacheContainer = new HashMap<>();
    private static final List<String> canCacheList = new ArrayList<>();
    private static final String CACHE_SEPARATOR = "#";
    private static final String CACHE_KEY = "%s" + CACHE_SEPARATOR + "%s";

    private ToolCacheManager() {
    }

    public static ToolCacheManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ToolCacheManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ToolCacheManager();
                }
            }
        }
        return INSTANCE;
    }

    public static void init(String... packageName) {
        getInstance().scanToolCacheAnnotation(packageName);
    }

    private void scanToolCacheAnnotation(String... packageName) {
        AnnotationProcessor processor = new AnnotationProcessor(ToolCache.class, packageName);
        List<Method> toolList = processor.getAnnotatedMethods();
        toolList.forEach(tool -> canCacheList.add(tool.getName()));
    }

    /**
     * 生成工具调用缓存的key
     * 格式: methodName#Arrays.toString(args)
     */
    public static String getCacheKey(Method method, Object... args) {
        return String.format(CACHE_KEY, method.getName(), Arrays.toString(args));
    }

    /**
     * 获取工具调用缓存
     */
    public Object getCachedTool(String key) {
        return toolCacheContainer.get(key);
    }

    /**
     * 添加工具调用到缓存
     */
    public void addCacheTool(String key, Object value) {
        toolCacheContainer.put(key, value);
    }

    /**
     * 判断方法是否被标记为可缓存方法
     */
    public boolean isCachedMethod(String toolName) {
        return canCacheList.contains(toolName);
    }

}
