package com.forsrc.utils;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThread {

    private ThreadPoolExecutor threadPoolExecutor;

    public MyThread() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 50,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public MyThread(int corePoolSize,
                    int maximumPoolSize,
                    long keepAliveTime,
                    TimeUnit unit,
                    int capacity) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, new ArrayBlockingQueue<Runnable>(capacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public <T> Future<T> submit(Runnable task) {
        return (Future<T>) this.threadPoolExecutor.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return (Future<T>) this.threadPoolExecutor.submit(task);
    }

    public void shutdown() {
        if (this.threadPoolExecutor == null) {
            return;
        }
        this.threadPoolExecutor.shutdown();
        try {
            while (!this.threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.threadPoolExecutor.shutdownNow();
            this.threadPoolExecutor = null;
        }
    }

    public void shutdownNow() {
        if (this.threadPoolExecutor == null) {
            return;
        }
        this.threadPoolExecutor.shutdownNow();
        this.threadPoolExecutor = null;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}
