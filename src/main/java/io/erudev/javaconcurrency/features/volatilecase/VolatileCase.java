package io.erudev.javaconcurrency.features.volatilecase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pengfei.zhao
 * @date 2023/11/4 13:13
 */
@Slf4j
public class VolatileCase {

    private  int x = 0;

    private volatile boolean flag = false;

    private void writer() {
        x = 42;
        flag = true;
    }

    private void reader() {
        if (flag) {
            log.debug("【x】={}", x);
        }
    }

    public static void main(String[] args) {
        VolatileCase test = new VolatileCase();
        test.writer();
        test.reader();
    }
}
