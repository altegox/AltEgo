package org.altegox.framework.config;

public class AltegoConfig {

    /**
     * 是否启用 ToolCache, 默认不启用
     */
    private static boolean enableCallCache = false;

    private static String fileSavePath;

    public static void enableToolCache() { enableCallCache = true; }

    public static void disableToolCache() { enableCallCache = false; }

    public static boolean isEnableCallCache() { return enableCallCache; }

    public static String getFileSavePath() { return fileSavePath; }

    public static void setFileSavePath(String fileSavePath) { AltegoConfig.fileSavePath = fileSavePath; }

}
