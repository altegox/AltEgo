package org.altegox.framework;

import org.altegox.framework.annotation.AnnotationProcessor;
import org.altegox.framework.annotation.Component;
import org.altegox.framework.annotation.Tool;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.toolcall.ComponentContainer;
import org.altegox.framework.toolcall.InstanceUtils;
import org.altegox.framework.toolcall.ToolCacheManager;
import org.altegox.framework.toolcall.ToolManager;
import org.altegox.framework.toolcall.ToolEntity;

import java.util.List;

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
        AnnotationProcessor toolProcessor = new AnnotationProcessor(Tool.class, packageName);
        toolProcessor.getAnnotatedMethods().forEach(method -> {
            ToolEntity tool = ToolEntity.of(method, method.getName(), null);
            ToolManager.getInstance().registerTool(tool);
        });
    }

    /**
     * 扫描 @Component 注解并实例化
     */
    private static void registerComponents(String... packageName) {
        AnnotationProcessor componentProcessor = new AnnotationProcessor(Component.class, packageName);
        List<Class<?>> annotatedClasses = componentProcessor.getAnnotatedClasses();
        ComponentContainer container = ComponentContainer.getInstance();
        annotatedClasses.stream()
                .filter(clazz -> !container.contains(clazz))
                .forEach(clazz -> container.register(clazz, InstanceUtils.createInstance(clazz)));
    }


}