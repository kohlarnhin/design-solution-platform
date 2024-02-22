package com.koh.threadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPool {
    private final BlockingQueue<Runnable> workQueue;
    private final Thread[] workers;
    private final AtomicInteger threadCount = new AtomicInteger(0);

    public CustomThreadPool(int nThreads) {
        workQueue = new LinkedBlockingQueue<>();
        workers = new Thread[nThreads];
        for (int i = 0; i < nThreads; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        try {
            workQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Runnable task = workQueue.take(); // 阻塞获取任务
                    task.run(); // 执行任务
                }
            } catch (InterruptedException e) {
                // 捕获到中断异常，线程退出
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        CustomThreadPool threadPool = new CustomThreadPool(5);

        // 提交任务
        for (int i = 0; i < 10; i++) {
            int taskNumber = i;
            threadPool.execute(() -> {
                System.out.println("Task " + taskNumber + " executed by thread: " + Thread.currentThread().getName());
            });
        }
    }
}
