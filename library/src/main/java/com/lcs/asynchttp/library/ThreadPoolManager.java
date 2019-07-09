package com.lcs.asynchttp.library;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static final ThreadPoolManager ourInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return ourInstance;
    }

    private ThreadPoolManager() {
        mThreadPoolExecutor =
                new ThreadPoolExecutor(
                        3,
                        10,
                        15,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(4),
                        new RejectedExecutionHandler() {
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                addTask(r);
                            }
                        });
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();
    // 创建延时队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask ht) {
        if (ht != null) {
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ThreadPoolExecutor mThreadPoolExecutor;

    // 创建核心线程
    public Runnable coreThread =
            new Runnable() {
                Runnable runn = null;

                @Override
                public void run() {
                    while (true) {
                        try {
                            runn = mQueue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mThreadPoolExecutor.execute(runn);
                    }
                }
            };

    public Runnable delayThread =
            new Runnable() {
                @Override
                public void run() {
                    HttpTask httpTask = null;
                    while (true) {
                        try {
                            httpTask = mDelayQueue.take();
                            if (httpTask.getRetryTime() < 3) {
                                mThreadPoolExecutor.execute(httpTask);
                                httpTask.setRetryTime(httpTask.getRetryTime() + 1);
                                Log.e(ThreadPoolManager.class.getSimpleName(), "===重试机制===");
                            } else {
                                Log.e(ThreadPoolManager.class.getSimpleName(), "===重试过多===");
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
}
