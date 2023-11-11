package io.erudev.javaconcurrency.features.synchronizedcase;

import io.erudev.javaconcurrency.util.ThreadDumpHelper;
import io.erudev.javaconcurrency.util.ThreadUtil;

import java.util.Scanner;

/**
 * @author pengfei.zhao
 * @date 2023/11/4 19:33
 */
public class SynchronizedConnection {

    static class UnsafeAccount {

        /**
         * 账户余额
         */
        private int balance;

        public UnsafeAccount(int balance) {
            this.balance = balance;
        }

        /**
         * 转账操作
         *
         * @param target 目标账户
         * @param amt    金额
         */
        void transfer(UnsafeAccount target, int amt) {
            if (balance >= amt) {
                this.balance -= amt;
                target.balance += amt;
            }
        }
    }

    static class SynchronizedTransferAccount {
        private int balance;

        private SynchronizedTransferAccount(int balance) {
            this.balance = balance;
        }

        private volatile boolean flag = true;

        private void breakLoop() {
            this.flag = false;
        }


        public static void main(String[] args) {
            SynchronizedTransferAccount a = new SynchronizedTransferAccount(200);
            SynchronizedTransferAccount b = new SynchronizedTransferAccount(200);
            SynchronizedTransferAccount c = new SynchronizedTransferAccount(200);
            Thread t1 = new Thread(() -> a.transfer(b, 100));
            Thread t2 = new Thread(() -> b.transfer(c, 100));
            t1.start();
            t2.start();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String v = scanner.next();
                if ("1".equals(v)) {
                    a.breakLoop();
                    b.breakLoop();
                    c.breakLoop();
                    break;
                }
            }

            System.out.println("a:" + a.balance);
            System.out.println("b:" + b.balance);
            System.out.println("c:" + c.balance);

            // 线程t1、线程t2 会同时进入临界区, 读到账户b的余额都是 200, 因此t1、t2执行后变成 100 或者 300
        }

        /**
         * 转账操作
         *
         * @param target 目标账户
         * @param amt    金额
         */
        synchronized void transfer(SynchronizedTransferAccount target, int amt) {
            if (balance >= amt) {
                while (flag) {
                }

                System.out.println(Thread.currentThread().getName() + "this amount" + this.balance + " target amount:" + target.balance);
                this.balance -= amt;
                target.balance += amt;
                System.out.println(Thread.currentThread().getName() + "this amount" + this.balance + " target amount:" + target.balance);

                // 临界区有两个需要保护的对象 this, target, synchronized 分别是锁的各自的对象
            }
        }
    }

    static class SafeAccount {

        /**
         * 转账操作需要同时传入 同一把lock
         * <p>
         * 相当于串行执行了, 性能不加不推荐
         */
        private final Object lock;

        private int balance;

        public SafeAccount(Object lock, int balance) {
            this.lock = lock;
            this.balance = balance;
        }

        void transfer(SafeAccount target, int amt) {
            synchronized (lock) {
                //synchronized (SafeAccount.class)  等价
                if (this.balance >= amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Object lock = new Object();
            SafeAccount a = new SafeAccount(lock, 200);
            SafeAccount b = new SafeAccount(lock, 200);
            SafeAccount c = new SafeAccount(lock, 200);
            Thread t1 = new Thread(() -> a.transfer(b, 100));
            Thread t2 = new Thread(() -> b.transfer(c, 100));

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            System.out.println("a:" + a.balance);
            System.out.println("b:" + b.balance);
            System.out.println("c:" + c.balance);

        }
    }

    static class DeadLockAccount {

        private static ThreadDumpHelper dumpHelper = new ThreadDumpHelper();

        private int balance;

        public DeadLockAccount(int balance) {
            this.balance = balance;
        }

        void transfer(DeadLockAccount target, int amt) {
            synchronized (this) {
                /**
                 * 死锁
                 * A、B两个账户
                 * 线程t1 A转给B
                 * 线程t2 B转给A
                 * t1 拿到A的锁的同时 t2 拿到B的锁
                 * t1 等待 B 的锁
                 * t2 等待 A 的锁
                 */

                ThreadUtil.sleep(2);

                synchronized (target) {
                    if (this.balance >= amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }

        public static void main(String[] args) {
            DeadLockAccount a = new DeadLockAccount(200);
            DeadLockAccount b = new DeadLockAccount(200);

            Thread t1 = new Thread(() -> a.transfer(b, 100));
            Thread t2 = new Thread(() -> b.transfer(a, 100));

            t1.start();
            t2.start();

            dumpHelper.tryThreadDump();
        }
    }

}
