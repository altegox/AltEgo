package org.altego.framework.toolcall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolManager {

    private static volatile ToolManager instance = null;

    private ToolManager() {
    }

    public static ToolManager getInstance() {
        if (instance == null) {
            synchronized (ToolManager.class) {
                if (instance == null) {
                    instance = new ToolManager();
                }
            }
        }
        return instance;
    }

    /* 工具容器 */
    private final Map<String, ToolEntity> toolContainer = new HashMap<>();

    public void registerTool(List<ToolEntity> toolList) {
        toolList.forEach(this::registerTool);
    }

    public void registerTool(ToolEntity tool) {
        toolContainer.put(tool.signature(), tool);
    }

    public ToolEntity getTool(String signature) {
        return toolContainer.get(signature);
    }

    public ToolEntity getTool(String toolname, Object... args) {
        Class<?>[] argTypes = Arrays.stream(args)
                .map(arg -> arg != null ? arg.getClass() : Object.class)
                .toArray(Class<?>[]::new);
        return getTool(ToolSigner.sign(toolname, argTypes));
    }

}
