package org.altegox.framework.toolcall;

import java.lang.reflect.Method;
import java.util.Map;

public class ToolEntity {

    private final String type;
    private final Function function;
    private final transient Method method;
    private final transient String signature;

    public static class Function {
        private final String name;
        private final String description;
        private final ToolParameters parameters;

        public Function(String name, String description, ToolParameters parameters) {
            this.name = name;
            this.description = description;
            this.parameters = parameters;
        }
    }

    public ToolEntity(String type, Method method, String name, String description, String signature,
                      ToolParameters parameters) {
        this.type = (type == null ? "function" : type);
        this.method = method;
        this.function = new Function(name, description, parameters);
        this.signature = signature;
    }

    public static ToolEntity of(String type, Method method, String toolName, String description,
                                ToolParameters parameters) {
        return new ToolEntity(type, method, toolName, description, ToolSigner.sign(method), parameters);
    }

    public static ToolEntity of(Method method, String toolName, String description, ToolParameters parameters) {
        return new ToolEntity(null, method, toolName, description, ToolSigner.sign(method), parameters);
    }

    public static ToolEntity of(Method method, String name, String description) {
        return new ToolEntity(null, method, name, description, ToolSigner.sign(method), null);
    }

    public String type() {
        return type;
    }

    public Method method() {
        return method;
    }

    public String toolName() {
        return function.name;
    }

    public String description() {
        return function.description;
    }

    public String signature() {
        return signature;
    }

    public ToolParameters parameters() {
        return function.parameters;
    }

    public static class ToolParameters {
        private String type = "object";
        private final Map<String, Property> properties;
        private final String[] required;

        public ToolParameters(String type, Map<String, Property> properties, String[] required) {
            this.type = type;
            this.properties = properties;
            this.required = required;
        }

        public ToolParameters(Map<String, Property> properties, String[] required) {
            this.properties = properties;
            this.required = required;
        }

        public static ToolParameters of(Map<String, Property> properties, String[] required) {
            return new ToolParameters(properties, required);
        }

        public static ToolParameters of(String type, Map<String, Property> properties, String[] required) {
            return new ToolParameters(type, properties, required);
        }

        public String type() {
            return type;
        }

        public Map<String, Property> properties() {
            return properties;
        }

        public String[] required() {
            return required;
        }
    }

    public record Property(String type, String description) {
        public static Property of(String type, String description) {
            return new Property(type, description);
        }
    }

}