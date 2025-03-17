package org.altegox.framework.toolcall;

import org.altegox.framework.config.MetaData;
import org.altegox.framework.annotation.AnnotationProcessor;
import org.altegox.framework.annotation.ToolCache;

import java.util.*;

public class ToolCacheManager {

    private static volatile ToolCacheManager INSTANCE = null;
    private static volatile boolean isInit = false;

    // Tool调用缓存容器
    private static final Map<String, Object> toolCache = new HashMap<>();
    // 可缓存Tool列表, 使用Tool Signature 作为 key
    private static final List<String> allowCacheList = new ArrayList<>();
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

    public synchronized static void init(String... packageName) {
        if (isInit) return;
        if (packageName == null) packageName = MetaData.basePackage;
        getInstance().scanToolCacheAnnotation(packageName);
        isInit = true;
    }

    private void scanToolCacheAnnotation(String... packageName) {
        AnnotationProcessor processor = new AnnotationProcessor(ToolCache.class, packageName);
        processor.getAnnotatedMethods().stream()
                .map(method -> ToolEntity.of(method, method.getName(), null))
                .forEach(tool -> allowCacheList.add(tool.signature()));
    }

    /**
     * 生成工具调用缓存的key
     * 格式: tool.signature()#Arrays.toString(args)
     */
    public static String getCacheKey(ToolEntity toolEntity, Object... args) {
        return String.format(CACHE_KEY, toolEntity.signature(), Arrays.toString(args));
    }

    /**
     * 获取工具调用缓存
     */
    public Object getCachedTool(String key) {
        return toolCache.get(key);
    }

    /**
     * 添加工具调用到缓存
     */
    public void addCacheTool(String key, Object value) {
        toolCache.put(key, value);
    }

    /**
     * 判断方法是否被标记为可缓存工具
     */
    public boolean isCachedMethod(String toolSignature) {
        return allowCacheList.contains(toolSignature);
    }

}
