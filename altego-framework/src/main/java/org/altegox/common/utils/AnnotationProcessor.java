package org.altegox.common.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.altegox.framework.annotation.Exclude;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AnnotationProcessor {

    private final List<Class<? extends Annotation>> annotations;

    private final String[] packageName;

    public AnnotationProcessor(List<Class<? extends Annotation>> annotation, String... packageName) {
        this.annotations = annotation;
        this.packageName = packageName;
    }

    public AnnotationProcessor(Class<? extends Annotation> annotation, String... packageName) {
        this.annotations = List.of(annotation);
        this.packageName = packageName;
    }

    public List<Class<?>> getAnnotatedClasses() {
        List<Class<?>> classes = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageName)
                .scan()) {

            scanResult.getAllClasses().forEach(classInfo -> {
                Class<?> clazz = classInfo.loadClass();
                for (Class<? extends Annotation> aClass : annotations) {
                    if (clazz.isAnnotationPresent(aClass)) {
                        classes.add(clazz);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    public List<Method> getAnnotatedMethods() {
        return getAnnotatedMethods(null);
    }

    /**
     * 获取指定包中使用指定注解的所有方法
     *
     * @return list of methods
     */
    public List<Method> getAnnotatedMethods(Predicate<Method> filter) {
        List<Method> methods = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageName)
                .scan()) {

            scanResult.getAllClasses().forEach(classInfo -> {
                Class<?> clazz = classInfo.loadClass();
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> isIncludedAnnotation(clazz, method)
                                && !isExcludedAnnotation(method))
                        .filter(method -> filter == null || filter.test(method))
                        .forEach(method -> {
                            method.setAccessible(true);
                            methods.add(method);
                        });
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return methods;
    }

    /**
     * 判断方法是否包含指定注解
     */
    private boolean isIncludedAnnotation(Class<?> clazz, Method method) {
        return annotations.stream()
                .anyMatch(aClass -> clazz.isAnnotationPresent(aClass)
                        || method.isAnnotationPresent(aClass));
    }

    /**
     * 判断方法是否包含 @Exclude 注解, 并且 @Exclude 注解中指定的注解是否在方法或类上
     */
    private boolean isExcludedAnnotation(Method method) {
        Exclude exclude = method.getAnnotation(Exclude.class);

        /* 若存在注解但无参数，则排除所有注解 */
        if (exclude == null || exclude.annotation().length == 0) {
            return exclude != null;
        }

        /* 若存在参数，则判断是否包含指定注解 */
        for (Class<? extends Annotation> aClass : exclude.annotation()) {
            if (annotations.contains(aClass)) {
                return true;
            }
        }
        return false;
    }

}
