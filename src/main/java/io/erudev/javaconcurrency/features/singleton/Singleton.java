package io.erudev.javaconcurrency.features.singleton;

/**
 * 单例
 * 双检锁
 *
 * @author pengfei.zhao
 * @date 2023/11/3 22:09
 */
public class Singleton {

    public static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
