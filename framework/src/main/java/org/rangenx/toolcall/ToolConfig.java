package org.rangenx.toolcall;

public class ToolConfig {

    /**
     * 是否启用 ToolCache, 默认不启用
     */
    private static boolean enableCallCache = false;

    public static void enableToolCache() {
        ToolConfig.enableCallCache = true;
    }

    public static void disableToolCache() {
        ToolConfig.enableCallCache = false;
    }

    // region Getter and Setter

    public static boolean isEnableToolCache() {
        return enableCallCache;
    }

    // endregion

}
