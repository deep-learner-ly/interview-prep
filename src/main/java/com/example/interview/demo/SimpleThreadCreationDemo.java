package com.example.interview.demo;

import java.util.concurrent.*;

/**
 * 线程创建方式 - 面试精简版演示
 * 突出面试重点，便于记忆和理解
 */
public class SimpleThreadCreationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Java线程创建方式（面试版）===\n");
        
        // 面试标准回答演示
        showInterviewAnswer();
        
        // 各种方式对比演示
        demonstrateComparison();
        
        // 面试重点总结
        showKeyPoints();
    }
    
    private static void showInterviewAnswer() {
        System.out.println("🎯 面试标准回答 - 5种创建线程方式：");
        System.out.println();
        System.out.println("1. 继承Thread类");
        System.out.println("   ✅ 语法简单，直接重写run()方法");
        System.out.println("   ❌ Java单继承限制，扩展性差");
        System.out.println();
        System.out.println("2. 实现Runnable接口");
        System.out.println("   ✅ 推荐方式，避免继承限制，可共享数据");
        System.out.println("   ❌ 无返回值，异常需要手动处理");
        System.out.println();
        System.out.println("3. 实现Callable接口");
        System.out.println("   ✅ 有返回值，可抛出异常");
        System.out.println("   ❌ 需要FutureTask包装，相对复杂");
        System.out.println();
        System.out.println("4. 使用线程池");
        System.out.println("   ✅ 资源复用，性能好，管理方便");
        System.out.println("   ❌ 需要合理配置参数");
        System.out.println();
        System.out.println("5. CompletableFuture（JDK8+）");
        System.out.println("   ✅ 支持异步编程，链式调用");
        System.out.println("   ❌ 学习成本较高");
        System.out.println();
    }
    
    private static void demonstrateComparison() {
        System.out.println("🚀 实际代码对比演示：");
        System.out.println();
        
        // 1. 继承Thread类
        System.out.println("1. Thread类方式：");
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println("   Thread方式执行 - " + Thread.currentThread().getName());
            }
        }
        new MyThread().start();
        
        // 2. Runnable接口
        System.out.println("2. Runnable方式：");
        Runnable runnable = () -> {
            System.out.println("   Runnable方式执行 - " + Thread.currentThread().getName());
        };
        new Thread(runnable).start();
        
        // 3. Callable接口
        System.out.println("3. Callable方式：");
        Callable<String> callable = () -> {
            System.out.println("   Callable方式执行 - " + Thread.currentThread().getName());
            return "执行完成";
        };
        FutureTask<String> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        
        // 4. 线程池
        System.out.println("4. 线程池方式：");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            System.out.println("   线程池方式执行 - " + Thread.currentThread().getName());
        });
        executor.shutdown();
        
        // 5. CompletableFuture
        System.out.println("5. CompletableFuture方式：");
        CompletableFuture.runAsync(() -> {
            System.out.println("   CompletableFuture方式执行 - " + Thread.currentThread().getName());
        });
        
        System.out.println();
    }
    
    private static void showKeyPoints() {
        System.out.println("📋 面试重点记忆：");
        System.out.println();
        System.out.println("✅ 实际开发推荐顺序：");
        System.out.println("   1. CompletableFuture（异步场景）");
        System.out.println("   2. 线程池（高并发场景）");
        System.out.println("   3. Runnable（一般场景）");
        System.out.println("   4. Callable（需要返回值）");
        System.out.println("   5. Thread类（简单场景）");
        System.out.println();
        
        System.out.println("💡 面试技巧：");
        System.out.println("• 重点掌握Runnable和线程池方式");
        System.out.println("• 了解Callable与Runnable的区别");
        System.out.println("• 熟悉CompletableFuture的基本用法");
        System.out.println("• 能说出各种方式的优缺点");
        System.out.println();
        
        System.out.println("⚠️ 常见陷阱：");
        System.out.println("• 直接调用run()方法不会创建新线程");
        System.out.println("• 线程池需要手动关闭");
        System.out.println("• 异常处理在不同方式中不同");
        System.out.println();
    }
}