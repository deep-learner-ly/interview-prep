package com.example.interview.demo;

import java.util.concurrent.*;

/**
 * 线程池拒绝策略完整演示程序
 * 详细展示4种内置拒绝策略的行为和应用场景
 * 
 * 面试题55参考：https://www.baeldung.com/java-threadpool-rejection-strategies
 */
public class ThreadPoolRejectionStrategiesDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Java线程池拒绝策略完整演示 ===\n");
        
        // 演示4种不同的拒绝策略
        demonstrateAllRejectionStrategies();
        
        System.out.println("\n=== 线程池拒绝策略演示完成 ===");
    }
    
    /**
     * 演示所有拒绝策略
     */
    private static void demonstrateAllRejectionStrategies() {
        demonstrateAbortPolicy();
        demonstrateCallerRunsPolicy();
        demonstrateDiscardPolicy();
        demonstrateDiscardOldestPolicy();
    }
    
    /**
     * 演示AbortPolicy - 默认策略，直接抛出异常
     */
    private static void demonstrateAbortPolicy() {
        System.out.println("1. AbortPolicy 演示 (默认策略，直接抛出异常):");
        
        // 创建一个极小容量的线程池用于演示拒绝
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1,  // 核心和最大线程数都是1
            60L, TimeUnit.SECONDS,  // 空闲线程存活时间
            new ArrayBlockingQueue<>(1),  // 只能容纳1个任务的队列
            new ThreadPoolExecutor.AbortPolicy()  // 设置拒绝策略
        );
        
        submitTasksWithLogging(executor, "AbortPolicy-Task", 5);
        
        shutdownExecutor(executor);
        System.out.println("   AbortPolicy 演示完成\n");
    }
    
    /**
     * 演示CallerRunsPolicy - 由调用线程处理任务
     */
    private static void demonstrateCallerRunsPolicy() {
        System.out.println("2. CallerRunsPolicy 演示 (由调用线程处理任务):");
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        submitTasksWithLogging(executor, "CallerRunsPolicy-Task", 5);
        
        shutdownExecutor(executor);
        System.out.println("   CallerRunsPolicy 演示完成\n");
    }
    
    /**
     * 演示DiscardPolicy - 丢弃任务不抛异常
     */
    private static void demonstrateDiscardPolicy() {
        System.out.println("3. DiscardPolicy 演示 (丢弃任务不抛异常):");
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardPolicy()
        );
        
        submitTasksWithLogging(executor, "DiscardPolicy-Task", 5);
        
        shutdownExecutor(executor);
        System.out.println("   DiscardPolicy 演示完成\n");
    }
    
    /**
     * 演示DiscardOldestPolicy - 丢弃队列头部任务
     */
    private static void demonstrateDiscardOldestPolicy() {
        System.out.println("4. DiscardOldestPolicy 演示 (丢弃队列头部任务):");
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2),  // 稍大一点的队列便于观察
            new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        
        submitTasksWithLogging(executor, "DiscardOldestPolicy-Task", 6);
        
        shutdownExecutor(executor);
        System.out.println("   DiscardOldestPolicy 演示完成\n");
    }
    
    /**
     * 提交任务并记录日志
     */
    private static void submitTasksWithLogging(ThreadPoolExecutor executor, String taskPrefix, int taskCount) {
        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            try {
                executor.submit(() -> {
                    System.out.println("   " + taskPrefix + "-" + taskId + " 开始执行，线程: " + 
                                     Thread.currentThread().getName());
                    try {
                        // 模拟任务执行时间
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("   " + taskPrefix + "-" + taskId + " 执行完成");
                });
                System.out.println("   " + taskPrefix + "-" + taskId + " 已提交");
            } catch (RejectedExecutionException e) {
                System.out.println("   " + taskPrefix + "-" + taskId + " 被拒绝执行: " + e.getMessage());
            }
        }
    }
    
    /**
     * 安全关闭线程池
     */
    private static void shutdownExecutor(ThreadPoolExecutor executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 自定义拒绝策略示例
     */
    public static class CustomRejectionPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("自定义拒绝策略: 任务被拒绝，尝试记录日志或发送告警");
            // 这里可以实现自定义的处理逻辑，比如：
            // - 将任务写入数据库进行持久化
            // - 发送告警通知
            // - 将任务保存到备用队列
        }
    }
    
    /**
     * 演示自定义拒绝策略
     */
    public static void demonstrateCustomPolicy() {
        System.out.println("5. 自定义拒绝策略演示:");
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new CustomRejectionPolicy()
        );
        
        submitTasksWithLogging(executor, "CustomPolicy-Task", 3);
        
        shutdownExecutor(executor);
        System.out.println("   自定义拒绝策略演示完成\n");
    }
}