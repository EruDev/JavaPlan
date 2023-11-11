package io.erudev.javaconcurrency.features.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier
 * 是一组线程之间相互等待
 *
 * @author pengfei.zhao
 * @date 2023/11/7 22:18
 */
public class CyclicBarrierCase {

    static class Worker extends Thread {
        private CyclicBarrier cb;

        public Worker(CyclicBarrier cb) {
            this.cb = cb;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                cb.await();
                System.out.println(Thread.currentThread().getName() + "开始执行");
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int threadCount = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);
        for (int i = 0; i < threadCount; i++) {
            System.out.println("创建工作线程" + i);
            Worker worker = new Worker(cyclicBarrier);
            worker.start();
        }
    }
}
