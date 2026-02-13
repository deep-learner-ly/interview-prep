package com.example.interview.demo;

import java.util.concurrent.*;

/**
 * 线程创建方式完整演示程序
 * 涵盖所有5种创建线程的方法
 * 
 * 面试题52参考：https://www.baeldung.com/java-thread-start
 */
public class ThreadCreationDemo {
    
    // 共享计数器用于演示
    private static int counter = 0;
    private static final Object lock = new Object();
    
    public static void main(String[] args) {
        System.out.println("=== Java线程创建方式完整演示 ===\n");
        
        // 1. 继承Thread类
        demonstrateThreadClass();
        
        // 2. 实现Runnable接口
        demonstrateRunnable();
        
        // 3. 实现Callable接口
        demonstrateCallable();
        
        // 4. 使用线程池
        demonstrateThreadPool();
        
        // 5. 使用CompletableFuture
        demonstrateCompletableFuture();
        
        System.out.println("\n=== 所有线程创建方式演示完成 ===");
    }
    
    /**
     * 1. 继承Thread类方式
     */
    private static void demonstrateThreadClass() {
        System.out.println("1. 继承Thread类方式:");
        
        class MyThread extends Thread {
            private String taskName;
            
            public MyThread(String taskName) {
                this.taskName = taskName;
            }
            
            @Override
            public void run() {
                System.out.println("   Thread类 - " + taskName + " 开始执行");
                for (int i = 0; i < 3; i++) {
                    synchronized (lock) {
                        counter++;
                        System.out.println("   " + taskName + " 计数: " + counter);
                    }
                    try {
                        Thread.sleep(100); // 模拟工作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("   Thread类 - " + taskName + " 执行完成");
            }
        }
        
        // 创建并启动线程
        MyThread thread1 = new MyThread("任务A");
        MyThread thread2 = new MyThread("任务B");
        
        thread1.start();
        thread2.start();
        
        // 等待线程完成
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   Thread类方式演示完成\n");
    }
    
    /**
     * 2. 实现Runnable接口方式
     */
    private static void demonstrateRunnable() {
        System.out.println("2. 实现Runnable接口方式:");
        
        class MyRunnable implements Runnable {
            private String taskName;
            
            public MyRunnable(String taskName) {
                this.taskName = taskName;
            }
            
            @Override
            public void run() {
                System.out.println("   Runnable - " + taskName + " 开始执行");
                for (int i = 0; i < 3; i++) {
                    synchronized (lock) {
                        counter++;
                        System.out.println("   " + taskName + " 计数: " + counter);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("   Runnable - " + taskName + " 执行完成");
            }
        }
        
        // 创建Runnable实例
        MyRunnable runnable1 = new MyRunnable("任务C");
        MyRunnable runnable2 = new MyRunnable("任务D");
        
        // 传入Thread构造函数
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   Runnable方式演示完成\n");
    }
    
    /**
     * 3. 实现Callable接口方式
     */
    private static void demonstrateCallable() {
        System.out.println("3. 实现Callable接口方式:");
        
        class MyCallable implements Callable<Integer> {
            private String taskName;
            private int workCount;
            
            public MyCallable(String taskName, int workCount) {
                this.taskName = taskName;
                this.workCount = workCount;
            }
            
            @Override
            public Integer call() throws Exception {
                System.out.println("   Callable - " + taskName + " 开始执行");
                int result = 0;
                for (int i = 0; i < workCount; i++) {
                    result += i;
                    System.out.println("   " + taskName + " 计算中: " + result);
                    Thread.sleep(100);
                }
                System.out.println("   Callable - " + taskName + " 执行完成，结果: " + result);
                return result;
            }
        }
        
        // 创建Callable实例
        MyCallable callable1 = new MyCallable("计算任务1", 3);
        MyCallable callable2 = new MyCallable("计算任务2", 2);
        
        // 使用FutureTask包装
        FutureTask<Integer> futureTask1 = new FutureTask<>(callable1);
        FutureTask<Integer> futureTask2 = new FutureTask<>(callable2);
        
        // 创建线程执行
        Thread thread1 = new Thread(futureTask1);
        Thread thread2 = new Thread(futureTask2);
        
        thread1.start();
        thread2.start();
        
        try {
            // 获取执行结果
            Integer result1 = futureTask1.get();
            Integer result2 = futureTask2.get();
            System.out.println("   Callable结果汇总: " + result1 + " + " + result2 + " = " + (result1 + result2));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        System.out.println("   Callable方式演示完成\n");
    }
    
    /**
     * 4. 使用线程池方式
     */
    private static void demonstrateThreadPool() {
        System.out.println("4. 使用线程池方式:");
        
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        class PoolTask implements Runnable {
            private String taskName;
            private int taskId;
            
            public PoolTask(int taskId, String taskName) {
                this.taskId = taskId;
                this.taskName = taskName;
            }
            
            @Override
            public void run() {
                System.out.println("   线程池任务 - " + taskName + " (ID:" + taskId + ") 开始执行");
                try {
                    Thread.sleep(200); // 模拟工作
                    System.out.println("   线程池任务 - " + taskName + " 执行完成");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        // 提交任务到线程池
        for (int i = 1; i <= 5; i++) {
            executor.submit(new PoolTask(i, "池任务" + i));
        }
        
        // 关闭线程池
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   线程池方式演示完成\n");
    }
    
    /**
     * 5. 使用CompletableFuture方式
     */
    private static void demonstrateCompletableFuture() {
        System.out.println("5. 使用CompletableFuture方式:");
        
        // 异步执行任务
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("   CompletableFuture任务1 开始执行");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "任务1完成";
        });
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("   CompletableFuture任务2 开始执行");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "任务2完成";
        });
        
        // 组合多个异步任务
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        
        // 处理结果
        combinedFuture.thenRun(() -> {
            try {
                String result1 = future1.get();
                String result2 = future2.get();
                System.out.println("   组合结果: " + result1 + ", " + result2);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        
        // 等待完成
        try {
            combinedFuture.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        
        System.out.println("   CompletableFuture方式演示完成\n");
    }
}