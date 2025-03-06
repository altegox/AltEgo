package org.altegox.framework.toolcall;

import java.lang.reflect.Method;
import java.util.*;

import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;

public class ToolSigner {

    private final static Map<Class<?>, String> primitiveToWrapper = Map.of(
            int.class, "Integer",
            long.class, "Long",
            double.class, "Double",
            float.class, "Float",
            boolean.class, "Boolean",
            char.class, "Character",
            byte.class, "Byte",
            short.class, "Short",
            void.class, "Void"
    );

    private static final Map<Class<?>, String> collectionTypes = Map.of(
            List.class, "List",
            ArrayList.class, "List",
            LinkedList.class, "List",
            Map.Entry.class, "MapEntry",
            Map.class, "Map",
            HashMap.class, "Map",
            LinkedHashMap.class, "Map",
            TreeMap.class, "Map",
            ConcurrentHashMap.class, "Map",
            Set.class, "Set"
    );

    private static String toWrapper(Class<?> paramType) {
        return paramType.isPrimitive()
                ? primitiveToWrapper.get(paramType)
                : paramType.getSimpleName();
    }

    private static String parseType(Type type) {
        if (type instanceof Class<?> clazz) {
            // 遍历集合类型映射，找到匹配的接口
            for (Map.Entry<Class<?>, String> entry : collectionTypes.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }
            return toWrapper(clazz);
        } else if (type instanceof ParameterizedType parameterizedType) {
            // 忽略泛型，只返回原始类型
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            return rawType.getSimpleName();
        } else {
            return type.getTypeName();
        }
    }

    public static String sign(ToolEntity tool) {
        return sign(tool.method());
    }

    public static String sign(String methodName, Object... params) {
        StringJoiner paramsJoiner = new StringJoiner(", ", methodName + "(", ")");

        Arrays.stream(params)
                .map(Object::getClass)
                .map(ToolSigner::parseType)
                .forEach(paramsJoiner::add);

        return paramsJoiner.toString();
    }

    public static String sign(String methodName, Class<?>... params) {
        StringJoiner paramsJoiner = new StringJoiner(", ", methodName + "(", ")");

        Arrays.stream(params)
                .map(ToolSigner::parseType)
                .forEach(paramsJoiner::add);

        return paramsJoiner.toString();
    }

    public static String sign(Method method) {
        String methodName = method.getName();
        StringJoiner paramsJoiner = new StringJoiner(", ", methodName + "(", ")");

        Arrays.stream(method.getGenericParameterTypes())
                .map(ToolSigner::parseType)
                .forEach(paramsJoiner::add);

        return paramsJoiner.toString();
    }
}
