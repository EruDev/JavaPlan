package io.erudev.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author pengfei.zhao
 * @date 2023-11-11 10:21
 */
public class MyClassLoader extends ClassLoader{

    private String classPath;

    public MyClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 输入流
        byte[] clasData = getData(name);
        if (clasData != null) {
            // 将字节数组转换为字节码对象
            return defineClass(name, clasData, 0, clasData.length);
        }

        return super.findClass(name);
    }

    /**
     *  加载类的字节码数据
     *
     * @param className 类的全限定名称
     * @return
     */
    private byte[] getData(String className) {
        String path = classPath + File.separator + className.replace(".", File.separator) + ".class";
        try (InputStream in = new FileInputStream(path);
             ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
