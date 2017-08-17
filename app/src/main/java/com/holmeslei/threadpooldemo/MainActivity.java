package com.holmeslei.threadpooldemo;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:   几种常用的线程池使用Demo
 * author         xulei
 * Date           2017/8/17 09:41
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testThreadPoolExecutor();
//        testFixedThreadPool();
//        testSingleThreadExecutor();
        testCachedThreadPool();
    }

    /**
     * corePoolSize     线程池中核心线程的数量
     * maximumPoolSize  线程池中最大线程数量
     * keepAliveTime    非核心线程的超时时长,当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收
     * unit             keepAliveTime参数的单位
     * workQueue        线程池中的任务队列,用来存储已经被提交但是尚未执行的任务，有四种：
     * 1.ArrayBlockingQueue：规定了大小的BlockingQueue，按照FIFO（先进先出）的方式来进行存取
     * 2.LinkedBlockingQueue：大小不确定的BlockingQueue，可传大小参数，不传则默认Integer.MAX_VALUE
     * 3.PriorityBlockingQueue：与LinkedBlockingQueue类似，但不是按照FIFO来排序的，按元素的Comparator来决定存取顺序
     * 4.SynchronousQueue：线程安全同步Queue，
     * threadFactory    为线程池提供创建新线程的功能,一般使用默认即可
     * handler          拒绝策略,一般是由于线程池中的线程数量已经达到最大数或者线程池关闭导致无法处理新线程时，会抛出一个RejectedExecutionException
     */
    private void testThreadPoolExecutor() {
        //通常实际开发中核心线程数我们取当前手机CUP数量+1，最大线程数为CUP数量*2+1，线程队列大小128
        int cupNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(cupNum + 1, cupNum * 2 + 1, 1, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(128));
        for (int i = 0; i < 50; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.e("testThreadPoolExecutor", "run：" + index);
                }
            };
            threadPoolExecutor.execute(runnable);
        }
    }

    /**
     * 一个核心线程数量固定的线程池
     * 1. 核心线程数和最大线程数一样，所有线程都是核心线程
     * 2. 超时时间为0，核心线程在没有任务执行时也不会被销毁
     * 3. 线程队列为无参LinkedBlockingQueue，队列大小为Integer.MAX_VALUE
     */
    private void testFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 50; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.e("testFixedThreadPool", "run：" + index);
                }
            };
            fixedThreadPool.execute(runnable);
        }
    }

    /**
     * 类似FixedThreadPool，不同的是其核心线程只有1个
     */
    private void testSingleThreadExecutor() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 50; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.e("testSingleThreadrPool", "run：" + index);
                }
            };
            singleThreadExecutor.execute(runnable);
        }
    }

    /**
     * 可根据程序的运行情况自动来调整线程池中的线程数量
     * 1. 无核心线程，最大线程数为Integer.MAX_VALUE
     * 2. 使用SynchronousQueue作为线程队列
     * 3. 超时时间为60秒
     * 4。有新任务时，若线程池中有空闲线程则使用空闲线程，无空闲的则创建新的线程来执行
     */
    private void testCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 50; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.e("testCachedThreadPool", "run：" + Thread.currentThread().getName() + "---" + index);
                }
            };
            SystemClock.sleep(1000);
            cachedThreadPool.execute(runnable);
        }
    }
}
