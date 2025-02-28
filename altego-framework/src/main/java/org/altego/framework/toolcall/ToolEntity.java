package org.altego.framework.toolcall;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Map;

public class ToolEntity {

    private final String type;
    private final transient Method method;
    private final String name;
    private final String description;
    private final transient String signature;
    private final ToolParameters parameters;

    public ToolEntity(String type, Method method, String name, String description, String signature, ToolParameters parameters) {
        this.type = (type == null ? "function" : type);
        this.method = method;
        this.name = name;
        this.description = description;
        this.signature = signature;
        this.parameters = parameters;
    }

    public static ToolEntity of(String type, Method method, String toolName, String description, ToolParameters parameters) {
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
        return name;
    }

    public String description() {
        return description;
    }

    public String signature() {
        return signature;
    }

    public ToolParameters parameters() {
        return parameters;
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