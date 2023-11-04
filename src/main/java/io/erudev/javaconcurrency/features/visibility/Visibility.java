package io.erudev.javaconcurrency.features.visibility;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author pengfei.zhao
 * @date 2023/11/3 21:49
 */
@Slf4j
public class Visibility {

    private volatile long count = 0;
    private AtomicLong count2 = new AtomicLong(0);

    private synchronized void add10k() {
        int i = 0;
        while (i++ < 10000) {
            count += 1;
        }
    }

    public static long cacl() throws InterruptedException {
        Visibility test = new Visibility();
        Thread t1 = new Thread(test::add10k);
        Thread t2 = new Thread(test::add10k);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        return test.count;
    }

    public static void main(String[] args) throws InterruptedException {
        long count = cacl();
        log.debug("【count】={}", count);
    }
}
