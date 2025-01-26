package org.rangenx;

import org.rangenx.annotation.AnnotationProcessor;
import org.rangenx.annotation.Tool;
import org.rangenx.config.RangenConfig;
import org.rangenx.toolcall.ToolCacheManager;
import org.rangenx.toolcall.ToolManager;
import org.rangenx.toolcall.ToolEntity;

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

    }

}