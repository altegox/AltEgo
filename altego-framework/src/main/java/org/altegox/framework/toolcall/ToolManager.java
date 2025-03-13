package org.altegox.framework.toolcall;

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
        /*
          如你所见，这里其实并不合理，因为toolname来源于method name，所以一个toolname可能对应多个Tool
          但是为了简单起见，这里只用一个工具名对应一个工具实体，即不允许出现重名的method
         */
        toolContainer.put(tool.toolName(), tool);
        /*
          这里的signature本来是用于唯一标识一个工具，但这会使得getTool变得复杂，现已改用toolname
          这里仅为兼容处理，后续将会去除
         */
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

    public List<ToolEntity> getToolsByName(String toolname) {
        return toolContainer.values().stream()
                .filter(tool -> tool.toolName().equals(toolname))
                .toList();
    }

    public ToolEntity getToolByName(String toolname) {
        return toolContainer.get(toolname);
    }

    public List<ToolEntity> getToolsByGroup(String group) {
        return toolContainer.values().stream()
                .filter(tool -> tool.group().equals(group))
                .toList();
    }

}
