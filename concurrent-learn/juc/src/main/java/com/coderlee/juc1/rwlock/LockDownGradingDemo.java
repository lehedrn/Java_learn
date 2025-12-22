package com.coderlee.juc1.rwlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁降级演示类
 *
 * 读写锁降级是指从写锁降级为读锁的过程，即先获取写锁，然后获取读锁，最后释放写锁，
 * 这样可以保证数据的可见性和一致性。
 *
 * 注意：ReentrantReadWriteLock不支持锁升级（从读锁升级为写锁），但支持锁降级。
 */
@Slf4j
public class LockDownGradingDemo {

    public static void main(String[] args) {
        // 演示错误的方式：尝试在持有读锁的情况下获取写锁
//        rightDemo();
//        rightDemo2();
        wrongDemo();
    }

    /**
     * 错误演示：试图在持有读锁的情况下获取写锁
     *
     * 这种方式会导致死锁，因为ReentrantReadWriteLock不允许在持有读锁的情况下获取写锁。
     * 写锁是独占的，不能与读锁共存，而当前线程已经持有了读锁，所以无法再获取写锁。
     */
    private static void wrongDemo() {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        readLock.lock();
        log.info("get readLock");
        // 在持有读锁的情况下尝试获取写锁会导致当前线程阻塞，从而造成死锁
        writeLock.lock();
        log.info("get writeLock");
        readLock.unlock();
        log.info("release readLock");
        writeLock.unlock();
        log.info("release writeLock");
    }

    /**
     * 正确演示2：锁降级的正确实现方式
     *
     * 先获取写锁，然后获取读锁，再释放写锁，最后释放读锁。
     * 这就是所谓的"锁降级"，即从写锁降级为读锁。
     */
    private static void rightDemo2() {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        log.info("get writeLock");
        readLock.lock();
        log.info("get readLock");
        // 先释放写锁，完成锁降级
        writeLock.unlock();
        log.info("release writeLock");
        readLock.unlock();
        log.info("release readLock");
    }

    /**
     * 正确演示1：分别获取和释放读锁和写锁
     *
     * 先获取读锁并释放，然后再获取写锁并释放，这是最基础的使用方式。
     */
    private static void rightDemo() {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        readLock.lock();
        log.info("get readLock");
        readLock.unlock();
        log.info("release readLock");

        writeLock.lock();
        log.info("get writeLock");
        writeLock.unlock();
        log.info("release writeLock");
    }
}
