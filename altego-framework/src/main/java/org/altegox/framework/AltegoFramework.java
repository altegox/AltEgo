package org.altegox.framework;

import org.altegox.common.log.Log;
import org.altegox.common.utils.Json;
import org.altegox.framework.annotation.*;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.config.MetaData;
import org.altegox.framework.toolcall.ComponentManager;
import org.altegox.framework.toolcall.InstanceUtils;
import org.altegox.framework.toolcall.ToolCacheManager;
import org.altegox.framework.toolcall.ToolManager;
import org.altegox.framework.toolcall.ToolEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltegoFramework {

    public synchronized static void init(String... packageName) {
        if (packageName == null || packageName.length == 0) {
            String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            int lastDot = callerClassName.lastIndexOf('.');
            String callerPackageName = (lastDot == -1) ? "" : callerClassName.substring(0, lastDot);
            packageName = new String[]{callerPackageName};
        }
        MetaData.basePackage = packageName;

        initCache(packageName);
        registerTools(packageName);
        registerComponents(packageName);
    }

    private static void initCache(String... packageName) {
        if (AltegoConfig.isEnableCallCache()) {
            ToolCacheManager.init(packageName);
        }
    }

    /**
     * 扫描 @Tool 注解并注册工具方法
     */
    private static void registerTools(String... packageName) {
        AnnotationProcessor toolProcessor = new AnnotationProcessor(List.of(Tool.class, Agent.class), packageName);
        toolProcessor.getAnnotatedMethods().forEach(method -> {
            Tool toolAnnotation = method.getAnnotation(Tool.class);
            String toolName = method.getName();
            String toolDescription = toolAnnotation.description();

            Class<?> declaringClass = method.getDeclaringClass();
            Agent agentAnnotation = declaringClass.getAnnotation(Agent.class);
            String group;
            if (agentAnnotation != null) {
                group = "default".equals(agentAnnotation.group()) ? toolAnnotation.group() : agentAnnotation.group();
            } else {
                group = toolAnnotation.group();
            }
            ToolEntity.ToolParameters parameters = null;
            Param[] params = toolAnnotation.params();
            if (params.length > 0) {
                Map<String, ToolEntity.Property> properties = new HashMap<>();
                List<String> requiredList = new ArrayList<>();
                for (Param param : params) {
                    // 这里假设所有参数类型都为 string
                    properties.put(param.param(), ToolEntity.Property.of("string", param.description()));
                    if (param.required()) {
                        requiredList.add(param.param());
                    }
                }
                parameters = new ToolEntity.ToolParameters(properties, requiredList.toArray(new String[0]));
            }
            ToolEntity toolEntity = ToolEntity.of(method, group, toolName, toolDescription, parameters);
            Log.debug("Register tool: {}", Json.toJson(toolEntity));
            ToolManager.getInstance().registerTool(toolEntity);
        });
    }

    /**
     * 扫描 @Component 注解并实例化
     */
    private static void registerComponents(String... packageName) {
        AnnotationProcessor componentProcessor = new AnnotationProcessor(Component.class, packageName);
        List<Class<?>> annotatedClasses = componentProcessor.getAnnotatedClasses();
        ComponentManager container = ComponentManager.getInstance();
        annotatedClasses.stream()
                .filter(clazz -> !container.contains(clazz))
                .forEach(clazz -> container.register(clazz, InstanceUtils.createInstance(clazz)));
    }


}