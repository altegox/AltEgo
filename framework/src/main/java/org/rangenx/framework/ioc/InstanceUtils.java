package org.rangenx.framework.ioc;

import org.rangenx.common.Log;

import java.lang.reflect.Constructor;

public class InstanceUtils {

    private static final ComponentContainer container = ComponentContainer.getInstance();

    public static Object createInstance(Class<?> declaringClass) {
        if (container.contains(declaringClass)) {
            return container.get(declaringClass);
        }
        try {
            Constructor<?>[] constructors = declaringClass.getConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] initArgs = new Object[parameterTypes.length];

                boolean canInstantiate = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    initArgs[i] = getConstructParams(parameterTypes[i]);
                    if (initArgs[i] == null && !parameterTypes[i].isPrimitive()) {
                        canInstantiate = false;
                        break;
                    }
                }

                if (canInstantiate) {
                    Object instance = constructor.newInstance(initArgs);
                    container.register(declaringClass, instance);
                    Log.info("Create instance of class: {}", declaringClass.getName());
                    return instance;
                }
            }

            throw new RuntimeException("No suitable constructor found for class: " + declaringClass.getName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of class: " + declaringClass.getName(), e);
        }
    }

    public static Object getConstructParams(Class<?> type) {
        if (type.isPrimitive()) {
            // 基本类型的默认值
            if (type == boolean.class) return false;
            if (type == char.class) return '\u0000';
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0d;
        }

        if (container.contains(type)) {
            return container.get(type);
        }

        return null;
    }


}
