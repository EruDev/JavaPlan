package io.erudev.javaconcurrency.features.synchronizedcase;

/**
 * 保护没有关联关系的多个资源
 * 银行账户余额和密码没用关联关系
 *
 * @author pengfei.zhao
 * @date 2023/11/4 14:39
 */
public class SynchronizedNoConnection {

    /**
     * 锁: 保护账号密码
     */
    final Object pwdLock = new Object();

    /**
     * 锁: 保护账号余额
     */
    final Object balLock = new Object();

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 账户密码
     */
    private String password;

    void updatePassword(String pwd) {
        synchronized (pwdLock) {
            this.password = pwd;
        }
    }

    String getPassword() {
        synchronized (pwdLock) {
            return password;
        }
    }

    void updateBalance(Integer bal) {
        synchronized (balLock) {
            if (this.balance > bal) {
                this.balance -= bal;
            }
        }
    }

    Integer getBalance() {
        synchronized (balLock) {
            return balance;
        }
    }
}
