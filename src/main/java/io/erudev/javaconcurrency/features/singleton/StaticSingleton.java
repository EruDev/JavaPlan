package io.erudev.javaconcurrency.features.singleton;

/**
 * 单例
 *
 * 采用静态内部类的方式
 *
 * @author pengfei.zhao
 * @date 2023/11/3 22:09
 */
public class StaticSingleton {

    private StaticSingleton() {
    }

    private static final class InstanceHolder {
        static final StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance() {
        return InstanceHolder.instance;
    }
}
