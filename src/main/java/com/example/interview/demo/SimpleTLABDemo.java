package com.example.interview.demo;

/**
 * 简单易懂的TLAB演示程序
 * 用生活化的比喻来解释TLAB概念
 */
public class SimpleTLABDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 简单理解TLAB ===\n");
        
        // 1. 生活化比喻
        explainWithAnalogy();
        
        // 2. 简单的性能对比
        simplePerformanceTest();
        
        // 3. 关键要点总结
        keyPointsSummary();
    }
    
    /**
     * 用生活化比喻解释TLAB
     */
    private static void explainWithAnalogy() {
        System.out.println("1. TLAB是什么？（生活化理解）");
        System.out.println("   想象一个办公室有4个员工要发传单：");
        System.out.println();
        System.out.println("   ❌ 没有TLAB的情况（传统方式）：");
        System.out.println("   - 所有员工都要到同一个文件柜拿纸张");
        System.out.println("   - 每次拿纸都要排队等待");
        System.out.println("   - 效率低，经常堵在文件柜前");
        System.out.println();
        System.out.println("   ✅ 有TLAB的情况（优化后）：");
        System.out.println("   - 每个员工都有自己的小抽屉");
        System.out.println("   - 需要纸张时直接从自己抽屉拿");
        System.out.println("   - 不用排队，速度快很多");
        System.out.println();
        
        System.out.println("   在Java中：");
        System.out.println("   - 办公室 = JVM堆内存");
        System.out.println("   - 员工 = 线程");
        System.out.println("   - 纸张 = 要创建的对象");
        System.out.println("   - 文件柜 = 堆内存分配器");
        System.out.println("   - 小抽屉 = TLAB（线程本地分配缓冲区）");
        System.out.println();
    }
    
    /**
     * 简单的性能测试对比
     */
    private static void simplePerformanceTest() {
        System.out.println("2. 简单性能测试：");
        
        int threadCount = 2;  // 用2个线程演示
        int objectsPerThread = 50000;  // 每个线程创建5万个对象
        
        System.out.println("   测试设置：");
        System.out.println("   - 线程数：" + threadCount + "个");
        System.out.println("   - 每线程创建对象数：" + objectsPerThread + "个");
        System.out.println("   - 对象大小：约64字节");
        System.out.println();
        
        // 创建测试线程
        Thread[] threads = new Thread[threadCount];
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < objectsPerThread; j++) {
                    // 创建小对象（就像员工从自己抽屉拿纸）
                    String[] smallObject = new String[4]; // 约64字节
                    smallObject[0] = "Thread" + threadId;
                    smallObject[1] = "Object" + j;
                    
                    // 偶尔使用一下，防止被优化掉
                    if (j % 10000 == 0) {
                        smallObject[0].length();
                    }
                }
            });
        }
        
        // 启动并等待完成
        for (Thread thread : threads) {
            thread.start();
        }
        
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long totalObjects = (long) threadCount * objectsPerThread;
        
        System.out.println("   测试结果：");
        System.out.println("   - 总耗时：" + duration + " 毫秒");
        System.out.println("   - 创建对象总数：" + totalObjects + " 个");
        System.out.println("   - 平均速度：" + (totalObjects / Math.max(duration, 1)) + " 个对象/毫秒");
        System.out.println();
    }
    
    /**
     * 关键要点总结
     */
    private static void keyPointsSummary() {
        System.out.println("3. TLAB关键要点：");
        System.out.println("   🎯 核心目的：让多线程创建对象更快");
        System.out.println("   🚀 工作原理：每个线程有自己的内存小区域");
        System.out.println("   💡 主要优势：减少线程间的竞争和等待");
        System.out.println("   ⚙️  默认开启：现代JVM自动启用，无需额外配置");
        System.out.println();
        
        System.out.println("4. 什么时候特别有用？");
        System.out.println("   ✅ 高并发应用（如Web服务器）");
        System.out.println("   ✅ 频繁创建对象的程序");
        System.out.println("   ✅ 多线程处理大量数据的应用");
        System.out.println();
        
        System.out.println("5. 简单记忆法：");
        System.out.println("   TLAB = Thread Local Allocation Buffer");
        System.out.println("   翻译：线程本地的分配缓冲区");
        System.out.println("   作用：给每个线程分配一个私人的小仓库");
        System.out.println();
    }
}