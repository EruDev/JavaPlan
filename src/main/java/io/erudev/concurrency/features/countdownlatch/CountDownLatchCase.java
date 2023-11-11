package io.erudev.javaconcurrency.features.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch
 * 主要用來解決一个线程等待多个线程的场景
 *
 * @author pengfei.zhao
 * @date 2023/11/7 22:07
 */
public class CountDownLatchCase {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch cdl = new CountDownLatch(2);
        service.execute(() -> {
            System.out.println("T1, " + Thread.currentThread().getName());
            cdl.countDown();
        });
        service.execute(() -> {
            System.out.println("T2, " + Thread.currentThread().getName());
            cdl.countDown();
        });

        cdl.await(1, TimeUnit.SECONDS);
        System.out.println("T3, " + Thread.currentThread().getName());
        service.shutdown();
    }
}
