package com.example.interview.demo;

/**
 * 线程生命周期完整演示程序
 * 详细展示线程的6种状态转换过程
 * 
 * 面试题53参考：https://www.baeldung.com/java-thread-lifecycle
 */
public class ThreadLifecycleDemo {
    
    // 用于演示阻塞状态的锁对象
    private static final Object lock = new Object();
    
    public static void main(String[] args) {
        System.out.println("=== Java线程生命周期完整演示 ===\n");
        
        // 演示各种线程状态
        demonstrateThreadStates();
        
        // 演示状态转换过程
        demonstrateStateTransitions();
        
        System.out.println("\n=== 线程生命周期演示完成 ===");
    }
    
    /**
     * 演示线程的各种状态
     */
    private static void demonstrateThreadStates() {
        System.out.println("1. 线程状态演示:");
        
        // NEW状态 - 线程对象已创建但未启动
        Thread newThread = new Thread(() -> {
            System.out.println("   执行中的线程，当前状态: " + Thread.currentThread().getState());
        });
        System.out.println("   NEW状态: " + newThread.getState());
        
        // 启动线程进入RUNNABLE状态
        newThread.start();
        System.out.println("   RUNNABLE状态: " + newThread.getState());
        
        // 创建阻塞状态演示线程
        Thread blockedThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("   获取锁成功，当前状态: " + Thread.currentThread().getState());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 先让一个线程持有锁
        synchronized (lock) {
            blockedThread.start();
            // 短暂等待让blockedThread进入BLOCKED状态
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   BLOCKED状态: " + blockedThread.getState());
        }
        
        // 等待线程完成
        try {
            newThread.join();
            blockedThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   TERMINATED状态: " + newThread.getState());
        System.out.println("   线程状态演示完成\n");
    }
    
    /**
     * 演示WAITING状态
     */
    private static void demonstrateWaitingState() {
        System.out.println("2. WAITING状态演示:");
        
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("   等待线程开始，当前状态: " + Thread.currentThread().getState());
                try {
                    // 进入WAITING状态
                    lock.wait();
                    System.out.println("   等待线程被唤醒，当前状态: " + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        waitingThread.start();
        
        // 短暂等待让线程进入WAITING状态
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   WAITING状态: " + waitingThread.getState());
        
        // 唤醒等待线程
        synchronized (lock) {
            lock.notify();
        }
        
        try {
            waitingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   WAITING状态演示完成\n");
    }
    
    /**
     * 演示TIMED_WAITING状态
     */
    private static void demonstrateTimedWaitingState() {
        System.out.println("3. TIMED_WAITING状态演示:");
        
        Thread timedWaitingThread = new Thread(() -> {
            System.out.println("   超时等待线程开始，当前状态: " + Thread.currentThread().getState());
            try {
                // 进入TIMED_WAITING状态
                Thread.sleep(2000);
                System.out.println("   超时等待结束，当前状态: " + Thread.currentThread().getState());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        timedWaitingThread.start();
        
        // 短暂等待让线程进入TIMED_WAITING状态
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   TIMED_WAITING状态: " + timedWaitingThread.getState());
        
        try {
            timedWaitingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   TIMED_WAITING状态演示完成\n");
    }
    
    /**
     * 演示完整的状态转换过程
     */
    private static void demonstrateStateTransitions() {
        System.out.println("4. 线程状态转换过程演示:");
        
        // 演示NEW -> RUNNABLE -> TERMINATED
        demonstrateBasicLifecycle();
        
        // 演示WAITING状态转换
        demonstrateWaitingState();
        
        // 演示TIMED_WAITING状态转换
        demonstrateTimedWaitingState();
        
        // 演示BLOCKED状态转换
        demonstrateBlockedState();
    }
    
    /**
     * 演示基本生命周期：NEW -> RUNNABLE -> TERMINATED
     */
    private static void demonstrateBasicLifecycle() {
        System.out.println("   基本生命周期演示 (NEW -> RUNNABLE -> TERMINATED):");
        
        Thread basicThread = new Thread(() -> {
            System.out.println("     线程执行中，状态: " + Thread.currentThread().getState());
            // 模拟一些工作
            for (int i = 0; i < 3; i++) {
                System.out.println("     工作步骤 " + (i + 1));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        System.out.println("     1. NEW状态: " + basicThread.getState());
        basicThread.start();
        System.out.println("     2. RUNNABLE状态: " + basicThread.getState());
        
        try {
            basicThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("     3. TERMINATED状态: " + basicThread.getState());
        System.out.println("   基本生命周期演示完成\n");
    }
    
    /**
     * 演示BLOCKED状态转换
     */
    private static void demonstrateBlockedState() {
        System.out.println("   BLOCKED状态转换演示:");
        
        Thread blockerThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("     阻塞演示 - 持有锁的线程开始工作");
                try {
                    Thread.sleep(1500); // 持有锁1.5秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("     阻塞演示 - 持有锁的线程工作完成");
            }
        });
        
        Thread blockedThread = new Thread(() -> {
            System.out.println("     阻塞演示 - 等待锁的线程开始");
            synchronized (lock) {
                System.out.println("     阻塞演示 - 获取锁成功，执行工作");
                System.out.println("     阻塞演示 - 工作完成");
            }
        });
        
        // 启动持有锁的线程
        blockerThread.start();
        
        // 短暂延迟后启动等待锁的线程
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        blockedThread.start();
        
        // 检查blockedThread的状态
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("     BLOCKED状态: " + blockedThread.getState());
        
        // 等待所有线程完成
        try {
            blockerThread.join();
            blockedThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   BLOCKED状态转换演示完成\n");
    }
    
    /**
     * 实用工具方法：打印线程状态信息
     */
    public static void printThreadState(String threadName, Thread thread) {
        System.out.printf("   %s 线程状态: %s%n", threadName, thread.getState());
    }
    
    /**
     * 实用工具方法：安全地等待指定时间
     */
    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("   睡眠被中断");
        }
    }
}