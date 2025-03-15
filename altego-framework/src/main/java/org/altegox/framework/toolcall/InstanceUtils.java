package org.altegox.framework.toolcall;

import org.altegox.common.log.Log;
import org.altegox.framework.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class InstanceUtils {

    private static final ComponentManager container = ComponentManager.getInstance();
    // 用于检测循环依赖：保存当前正在构造的类集合
    private static final ThreadLocal<Set<Class<?>>> constructing = ThreadLocal.withInitial(HashSet::new);

    public static Object createInstance(Class<?> declaringClass) {
        if (container.contains(declaringClass)) {
            return container.get(declaringClass);
        }
        // 检查是否存在循环依赖
        if (constructing.get().contains(declaringClass)) {
            throw new RuntimeException("Circular dependency detected for class: " + declaringClass.getName());
        }
        constructing.get().add(declaringClass);
        try {
            Constructor<?>[] constructors = declaringClass.getConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] initArgs = new Object[parameterTypes.length];

                boolean canInstantiate = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    Object paramInstance = getConstructParams(parameterTypes[i]);
                    // 如果参数不是原始类型且无法解析依赖，则此构造方法不可用
                    if (paramInstance == null && !parameterTypes[i].isPrimitive()) {
                        canInstantiate = false;
                        break;
                    }
                    initArgs[i] = paramInstance;
                }

                if (canInstantiate) {
                    Object instance = constructor.newInstance(initArgs);
                    Log.debug("Create instance of class: {}", declaringClass.getName());
                    return instance;
                }
            }

            throw new RuntimeException("No suitable constructor found for class: " + declaringClass.getName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of class: " + declaringClass.getName(), e);
        } finally {
            constructing.get().remove(declaringClass);
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

        // 如果该类型被@Component标记，则递归创建该依赖
        if (type.isAnnotationPresent(Component.class)) {
            return createInstance(type);
        }

        return null;
    }

}
