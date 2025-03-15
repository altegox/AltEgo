package org.altegox.framework.toolcall;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {

    private ComponentManager() {
    }

    private static volatile ComponentManager instance = null;

    public static ComponentManager getInstance() {
        if (instance == null) {
            synchronized (ComponentManager.class) {
                if (instance == null) {
                    instance = new ComponentManager();
                }
            }
        }
        return instance;
    }

    private final Map<Class<?>, Object> container = new HashMap<>();

    public void register(Class<?> clazz, Object instance) {
        container.put(clazz, instance);
    }

    public Object get(Class<?> clazz) {
        return container.get(clazz);
    }

    public boolean contains(Class<?> clazz) {
        return container.containsKey(clazz);
    }
}
