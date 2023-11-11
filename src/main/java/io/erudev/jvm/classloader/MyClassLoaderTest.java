package io.erudev.jvm.classloader;

import java.lang.reflect.Method;

/**
 * @author pengfei.zhao
 * @date 2023-11-11 10:25
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws Exception {
        MyClassLoader classLoader = new MyClassLoader("D:\\lib");
        Class<?> clazz = classLoader.loadClass("io.erudev.jvm.classloader.Test");

        Object obj = clazz.newInstance();

        Method method = clazz.getMethod("say", null);
        method.invoke(obj, null);
    }
}
