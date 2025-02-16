package org.rangenx.framework.ioc;

import java.util.HashMap;
import java.util.Map;

public class ComponentContainer {

    private ComponentContainer() {
    }

    private static volatile ComponentContainer instance = null;

    public static ComponentContainer getInstance() {
        if (instance == null) {
            synchronized (ComponentContainer.class) {
                if (instance == null) {
                    instance = new ComponentContainer();
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
