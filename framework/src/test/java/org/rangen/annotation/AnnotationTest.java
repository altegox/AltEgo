package org.rangen.annotation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rangenx.framework.RangenFramework;
import org.rangenx.common.Log;
import org.rangenx.framework.config.RangenConfig;
import org.rangenx.framework.toolcall.ToolCacheManager;
import org.rangenx.framework.toolcall.ToolEntity;
import org.rangenx.framework.toolcall.ToolManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AnnotationTest {

    @BeforeAll
    static void initialize() {
        RangenConfig.enableToolCache();
        RangenFramework.init("org.rangen");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testToolContainer() throws IllegalAccessException {
        System.out.println("-------------- ToolManager --------------");
        ToolManager instance = ToolManager.getInstance();
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getName().equals("toolContainer")) {
                Map map = (Map) declaredField.get(instance);
                assertNotNull(map, "toolContainer should not be null");
                map.forEach((key, value) -> {
                    ToolEntity tool = (ToolEntity) value;
                    Method method = tool.method();
                    Log.info("toolname: {}, tool: {}", key, method.toGenericString());
                });
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCacheContainer() throws IllegalAccessException {
        System.out.println("-------------- ToolCacheManager --------------");
        ToolCacheManager instance = ToolCacheManager.getInstance();
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getName().equals("toolCache")) {
                Map map = (Map) declaredField.get(instance);
                map.forEach((key, value) -> {
                    Log.info("toolname: {}, tool: {}", key, value);
                });
            }
            if (declaredField.getName().equals("allowCacheList")) {
                List allowCacheList = (List) declaredField.get(instance);
                assertNotNull(allowCacheList, "toolContainer should not be null");
                allowCacheList.forEach(item -> {
                    Log.info("allowCacheList: {}", item);
                });
            }
        }
    }

}


