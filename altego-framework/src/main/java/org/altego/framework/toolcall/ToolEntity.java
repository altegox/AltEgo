package org.altego.framework.toolcall;

import java.lang.reflect.Method;

public record ToolEntity(Method method, String toolName, String description, String signature) {

    public static ToolEntity of(Method method, String toolName, String description) {
        return new ToolEntity(method, toolName, description, ToolSigner.sign(method));
    }

}