package com.forsrc.utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyLock {
    private ReentrantReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;

    public MyLock() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public void todoReadLock(TodoReadLock todoReadLock) throws Exception {
        this.readLock.lock();
        try {
            todoReadLock.todo();
        } catch (Exception e) {
            throw e;
        } finally {
            this.readLock.unlock();
        }
    }

    public void todoWriteLock(TodoWriteLock todoWriteLock) throws Exception {
        this.writeLock.lock();
        try {
            todoWriteLock.todo();
        } catch (Exception e) {
            throw e;
        } finally {
            this.writeLock.unlock();
        }
    }

    public Lock getReadLock() {
        return readLock;
    }

    public Lock getWriteLock() {
        return writeLock;
    }

    public static interface TodoReadLock {
        public void todo() throws Exception;
    }

    public static interface TodoWriteLock {
        public void todo() throws Exception;
    }

}
