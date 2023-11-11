package io.erudev.javaconcurrency.features.synchronizedcase;

import java.util.ArrayList;
import java.util.List;

/**
 * Synchronized 实现原理 基于操作系统Mutex Lock (互斥锁)实现，所以每次获取和释放都会由用户态和内核态的切换成本高，jdk1.5之前性能差
 * JVM通过ACC_SYNCHRONIZED 标识一个方法是否为同步方法,而代码块则通过monitorenter和monitorexit指令操作monitor对象
 *
 * @author pengfei.zhao
 * @date 2023/11/4 14:15
 */
public class SynchronizedCase {

    static long y = 0;

    static class SafeCacl {
        long x = 0;

        synchronized long get() {
            return y;
        }

        synchronized void addOne() {
            y += 1;
        }
    }

    public static void main(String[] args) {

        List<Thread> threadList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    SafeCacl sc = new SafeCacl();
                    sc.addOne();
                }
            });
            threadList.add(t);
        }

        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }

        }
        System.out.println(y);
    }
}
