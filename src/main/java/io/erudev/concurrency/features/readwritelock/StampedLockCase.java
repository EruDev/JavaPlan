package io.erudev.javaconcurrency.features.readwritelock;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock
 * 写锁和悲观锁, 跟 ReentrantReadWriteLock 类似, 非可重入锁, 并且不支持条件变量
 * ReadWriteLock 支持多个线程同时读, 但是多个线程同时读的时候
 * 所有的写操作会被阻塞, 而 StampedLock 提供的乐观锁(无锁), 是允许一个线程获取写锁的, 也就是说不是所有的写锁都被阻塞
 *
 * @author pengfei.zhao
 * @date 2023/11/7 21:51
 */
public class StampedLockCase {

    private final StampedLock sl = new StampedLock();

    /**
     * 获取 / 释放悲观锁
     *
     * @return
     */
    Object get() {
        long stamp = sl.readLock();
        try {
            return new Object();
            // 省略相关代码
        } finally {
            sl.unlockRead(stamp);
        }
    }

    void write() {
        long stamp = sl.writeLock();
        try {
            // ...
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    static class Point {
        private int x, y;
        final StampedLock sl = new StampedLock();

        Double distanceFromOrigin() {
            // 乐观锁
            long stamp = sl.tryOptimisticRead();
            // 读入局部变量, 读的过程可能被修改
            int curX = x, curY = y;

            // 判断执行读操作期间，
            // 是否存在写操作，如果存在，
            // 则 sl.validate 返回 false
            if (!sl.validate(stamp)) {
                // 升级为悲观读锁
                stamp = sl.readLock();
                try {
                    curX = x;
                    curY = y;
                } finally {
                    sl.unlockWrite(stamp);
                }
            }
            return Math.sqrt(curX * curX + curY * curY);
        }
    }
}
