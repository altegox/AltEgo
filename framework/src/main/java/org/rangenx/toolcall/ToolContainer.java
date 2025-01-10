package org.rangenx.toolcall;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolContainer {

    private static  volatile ToolContainer instance = null;

    private ToolContainer() {
    }

    public static ToolContainer getInstance() {
        if (instance == null) {
            synchronized (ToolContainer.class) {
                if (instance == null) {
                    instance = new ToolContainer();
                }
            }
        }
        return instance;
    }

    /* 可用工具容器 */
    private final Map<String, Method> toolContainer = new HashMap<>();

    public void registerToolList(List<Method> toolList) {
        toolList.forEach(this::registerAvailableMethod);
    }

    public void registerAvailableMethod(Method tool) {
        toolContainer.put(tool.getName(), tool);
    }

    public Method getTool(String toolName) {
        return toolContainer.get(toolName);
    }

}
