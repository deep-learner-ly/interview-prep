package com.example.interview.demo;

/**
 * TLAB (Thread Local Allocation Buffer) 演示程序
 * 展示线程本地分配缓冲区的工作原理和性能优势
 * 
 * 面试题47参考：https://www.baeldung.com/jvm-thread-local-arena
 */
public class TLABDemo {
    
    // 测试对象类
    static class TestObject {
        private int id;
        private String name;
        private double value;
        
        public TestObject(int id, String name, double value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestObject that = (TestObject) obj;
            return id == that.id && 
                   Double.compare(that.value, value) == 0 && 
                   java.util.Objects.equals(name, that.name);
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(id, name, value);
        }
        
        @Override
        public String toString() {
            return "TestObject{id=" + id + ", name='" + name + "', value=" + value + '}';
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== TLAB (Thread Local Allocation Buffer) 演示 ===\n");
        
        // 1. 显示JVM TLAB相关参数
        showTLABParameters();
        
        // 2. 演示多线程对象分配性能对比
        demonstrateMultiThreadAllocation();
        
        // 3. 展示TLAB大小对性能的影响
        demonstrateTLABSizeImpact();
        
        // 4. 展示TLAB概念和内存布局
        showTLABConcept();
        
        System.out.println("\n=== 演示完成 ===");
    }
    
    /**
     * 显示当前JVM的TLAB相关参数
     */
    private static void showTLABParameters() {
        System.out.println("1. JVM TLAB 参数信息:");
        System.out.println("   -XX:+UseTLAB: " + 
            java.lang.management.ManagementFactory.getRuntimeMXBean()
                .getInputArguments().contains("-XX:+UseTLAB"));
        System.out.println("   默认TLAB大小: 约Eden区的1/1024");
        System.out.println("   TLAB状态: 默认启用（现代JVM自动启用）");
        System.out.println();
    }
    
    /**
     * 演示多线程对象分配性能对比
     */
    private static void demonstrateMultiThreadAllocation() {
        System.out.println("2. 多线程对象分配性能测试:");
        
        int threadCount = 4;
        int objectsPerThread = 100000;
        
        // 测试不使用TLAB的情况（模拟）
        long startTime = System.nanoTime();
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < objectsPerThread; j++) {
                    // 模拟对象分配 - 在实际TLAB中这些会在本地缓冲区分配
                    TestObject obj = new TestObject(j, "Thread-" + threadId + "-Object-" + j, Math.random());
                    // 简单使用对象避免被优化掉
                    if (obj.id % 10000 == 0) {
                        obj.toString();
                    }
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // 转换为毫秒
        
        System.out.println("   创建对象总数: " + (threadCount * objectsPerThread));
        System.out.println("   分配耗时: " + duration + " ms");
        System.out.println("   平均每毫秒创建对象数: " + 
            (threadCount * objectsPerThread) / Math.max(duration, 1));
        System.out.println("   TLAB优势: 减少了堆内存分配的锁竞争");
        System.out.println();
    }
    
    /**
     * 演示不同对象大小对TLAB的影响
     */
    private static void demonstrateTLABSizeImpact() {
        System.out.println("3. 对象大小对TLAB分配的影响:");
        
        // 小对象测试
        System.out.println("   小对象分配测试:");
        testObjectAllocation(16, 100000); // 16字节小对象
        
        // 中等对象测试
        System.out.println("   中等对象分配测试:");
        testObjectAllocation(128, 50000); // 128字节中等对象
        
        // 大对象测试（可能直接在老年代分配）
        System.out.println("   大对象分配测试:");
        testObjectAllocation(1024, 10000); // 1KB大对象
    }
    
    /**
     * 测试指定大小的对象分配性能
     */
    private static void testObjectAllocation(int objectSize, int count) {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < count; i++) {
            // 创建指定大小的对象（通过数组模拟大小）
            byte[] data = new byte[objectSize - 16]; // 减去对象头大小
            TestObject obj = new TestObject(i, "SizeTest-" + objectSize, data.length);
            
            // 简单使用避免优化
            if (i % 10000 == 0) {
                obj.toString();
            }
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        
        System.out.println("     对象大小: " + objectSize + " 字节");
        System.out.println("     分配数量: " + count);
        System.out.println("     耗时: " + duration + " ms");
        System.out.println("     平均分配时间: " + (duration * 1000.0 / count) + " 微秒/对象");
        System.out.println();
    }
    
    /**
     * 展示TLAB的内存布局概念
     */
    private static void showTLABConcept() {
        System.out.println("4. TLAB 工作原理:");
        System.out.println("   ┌─────────────────────────────────────────┐");
        System.out.println("   │              Eden 区                    │");
        System.out.println("   ├─────────┬─────────┬─────────┬─────────┤");
        System.out.println("   │ Thread1 │ Thread2 │ Thread3 │ Thread4 │");
        System.out.println("   │  TLAB   │  TLAB   │  TLAB   │  TLAB   │");
        System.out.println("   │         │         │         │         │");
        System.out.println("   │ [obj1]  │ [obj3]  │ [obj5]  │ [obj7]  │");
        System.out.println("   │ [obj2]  │ [obj4]  │ [obj6]  │ [obj8]  │");
        System.out.println("   └─────────┴─────────┴─────────┴─────────┘");
        System.out.println();
        System.out.println("   优势:");
        System.out.println("   - 每个线程有自己的分配区域，无需同步");
        System.out.println("   - 快速指针移动完成分配");
        System.out.println("   - 减少堆内存分配的锁竞争");
        System.out.println("   - 提高多线程应用的吞吐量");
        System.out.println();
    }
}