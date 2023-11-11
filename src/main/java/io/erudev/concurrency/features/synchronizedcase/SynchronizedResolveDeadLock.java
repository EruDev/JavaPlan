package io.erudev.javaconcurrency.features.synchronizedcase;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决死锁问题
 *
 * @author pengfei.zhao
 * @date 2023/11/5 14:29
 */
public class SynchronizedResolveDeadLock {

    class Account {
        private Allocator allocator;
        private int balance;

        private void transfer(Account target, int amt) {
            while (!allocator.apply(this, target)) {
            }
            try {
                if (this.balance >= amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            } finally {
                allocator.free(this, target);
            }
        }
    }

    /**
     * 破坏占用且等待条件
     */
    class Allocator {
        private List<Object> list = new ArrayList<>();

        /**
         * 一次性申请所有资源
         *
         * @param from 转出账户
         * @param to   转入账户
         * @return boolean
         */
        synchronized boolean apply(Object from, Object to) {
            if (list.contains(from) || list.contains(to)) {
                return false;
            } else {
                list.add(from);
                list.add(to);
                return true;
            }
        }

        /**
         * 归还资源
         *
         * @param from 转出账户
         * @param to   转入账户
         */
        private void free(Object from, Object to) {
            list.remove(from);
            list.remove(to);
        }
    }

    /**
     * 根据id大小来加锁, 避免死锁, 成本相对较低
     */
    class SortAccount {
        private int id;
        private int balance;

        public SortAccount(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        synchronized void transfer(SortAccount target, int amt) {
            SortAccount from = this;
            SortAccount to = target;
            if (this.id > target.id) {
                from = target;
                to = this;
            }

            synchronized (from) {
                synchronized (to) {
                    if (this.balance >= amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }
    }

    /**
     * 等待-通知机制
     */
    static class WaitNotifyAllocator {

        private WaitNotifyAllocator() {}

        private List<Object> als;

        synchronized void apply(Object from, Object to) {
            while (als.contains(from) || als.contains(to)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            als.add(from);
            als.add(to);
        }

        synchronized void free(Object from, Object to) {
            als.remove(from);
            als.remove(to);
            notifyAll();
        }

        private WaitNotifyAllocator getInstance() {
            return WaitNotifySingleton.instance;
        }

        static class WaitNotifySingleton {
            static WaitNotifyAllocator instance = new WaitNotifyAllocator();
        }
    }

}
