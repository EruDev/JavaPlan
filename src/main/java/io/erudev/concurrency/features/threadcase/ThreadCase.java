package io.erudev.javaconcurrency.features.threadcase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pengfei.zhao
 * @date 2023/11/4 13:27
 */
@Slf4j
public class ThreadCase {

    static volatile int x = 1;

    public static void main(String[] args) throws InterruptedException {
        /**
         * 主线程启动子线程t1后, 子线程t1内可以看到主线程对x的修改, 也就是200
         */
        Thread t1 = new Thread(
                () -> log.debug("【x】= {}", x)
        );
        x = 200;
        t1.start();

        log.debug("===========================================================");

        /**
         * 主线程调用子线程join返回后, 可以看到子线程对x的操作, 也就是100
         */
        Thread t2 = new Thread(
                () -> x = 100
        );
        t2.start();
        t2.join();

        log.debug("【x】={}", x);
    }
}
