package io.erudev.javaconcurrency.features.semaphore;

import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 模型:
 * 一个等待队列,一个计数器,三个方法(init, acquire, release)
 *
 * @author pengfei.zhao
 * @date 2023/11/6 22:19
 */
public class SemaphoreCase {

    static class MutexSemaphore {

        private static int count;

        /**
         * 初始化信号量 permits 为1, 表示只允许一个线程进入临界区
         */
        private static final Semaphore semaphore = new Semaphore(1);

        static void addOne() {
            try {
                semaphore.acquire();
                count+=1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }

        }
    }
}
