package org.rangenx.framework;

import org.rangenx.framework.annotation.AnnotationProcessor;
import org.rangenx.framework.annotation.Component;
import org.rangenx.framework.annotation.Tool;
import org.rangenx.framework.config.RangenConfig;
import org.rangenx.framework.ioc.ComponentContainer;
import org.rangenx.framework.ioc.InstanceUtils;
import org.rangenx.framework.toolcall.ToolCacheManager;
import org.rangenx.framework.toolcall.ToolManager;
import org.rangenx.framework.toolcall.ToolEntity;

import java.util.List;

public class RangenFramework {

    public static void init(String... packageName) {
        // 如果为null，则使用调用这个函数的类所在的包的包名
        if (packageName == null || packageName.length == 0) {
            String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String callerPackageName = callerClassName.substring(0, callerClassName.lastIndexOf('.'));
            packageName = new String[]{callerPackageName};
        }
        MetaData.basePackage = packageName;
        if (RangenConfig.isEnableCallCache()) {
            ToolCacheManager.init(packageName);
        }

        AnnotationProcessor processor = new AnnotationProcessor(Tool.class, packageName);
        processor.getAnnotatedMethods().forEach(method -> {
            ToolEntity tool = ToolEntity.of(method, method.getName(), null);
            ToolManager.getInstance().registerTool(tool);
        });

        AnnotationProcessor iocProcessor = new AnnotationProcessor(Component.class, packageName);
        List<Class<?>> annotatedClasses = iocProcessor.getAnnotatedClasses();

        ComponentContainer container = ComponentContainer.getInstance();

        for (Class<?> clazz : annotatedClasses) {
            Object instance = InstanceUtils.createInstance(clazz);
            container.register(clazz, instance);
        }

    }

}