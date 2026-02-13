package com.example.interview.demo;

/**
 * TLAB 调优演示程序
 * 展示不同TLAB参数对性能的影响
 * 
 * 相关JVM参数:
 * -XX:+UseTLAB (默认启用)
 * -XX:TLABSize=<size> (设置TLAB大小)
 * -XX:TLABWasteTargetPercent=<percent> (TLAB浪费目标百分比)
 * -XX:-ResizeTLAB (禁用TLAB动态调整)
 */
public class TLABTuningDemo {
    
    static class WorkerThread extends Thread {
        private final int objectCount;
        private final int objectSize;
        private long allocationTime;
        
        public WorkerThread(int objectCount, int objectSize) {
            this.objectCount = objectCount;
            this.objectSize = objectSize;
        }
        
        @Override
        public void run() {
            long startTime = System.nanoTime();
            
            // 分配指定数量和大小的对象
            for (int i = 0; i < objectCount; i++) {
                byte[] data = new byte[objectSize];
                // 简单使用避免被JIT优化掉
                if (i % 1000 == 0) {
                    data[0] = (byte)(i & 0xFF);
                }
            }
            
            long endTime = System.nanoTime();
            allocationTime = endTime - startTime;
        }
        
        public long getAllocationTime() {
            return allocationTime;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== TLAB 性能调优演示 ===\n");
        
        // 1. 基准测试 - 默认TLAB配置
        System.out.println("1. 默认TLAB配置性能测试:");
        runPerformanceTest(4, 100000, 64, "默认配置");
        
        // 2. 大TLAB测试
        System.out.println("\n2. 大TLAB配置模拟:");
        System.out.println("   (实际需要JVM参数: -XX:TLABSize=1024k)");
        runPerformanceTest(4, 50000, 512, "大对象分配");
        
        // 3. 小对象高频率分配测试
        System.out.println("\n3. 小对象高频率分配测试:");
        runPerformanceTest(8, 200000, 32, "小对象高频");
        
        // 4. 展示TLAB相关JVM参数
        showTLABJVMParameters();
        
        System.out.println("\n=== 调优建议 ===");
        System.out.println("• 对象分配频繁的应用: 增大TLAB大小");
        System.out.println("• 对象大小较均匀: 适当调整TLABWasteTargetPercent");
        System.out.println("• 内存敏感应用: 可考虑减小TLAB大小");
        System.out.println("• 多线程高并发: TLAB能显著提升性能");
    }
    
    private static void runPerformanceTest(int threadCount, int objectsPerThread, 
                                         int objectSize, String testName) {
        System.out.println("   测试场景: " + testName);
        System.out.println("   线程数: " + threadCount);
        System.out.println("   每线程对象数: " + objectsPerThread);
        System.out.println("   对象大小: " + objectSize + " 字节");
        
        WorkerThread[] threads = new WorkerThread[threadCount];
        
        // 创建并启动线程
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new WorkerThread(objectsPerThread, objectSize);
            threads[i].start();
        }
        
        // 等待所有线程完成
        try {
            for (WorkerThread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // 计算统计信息
        long totalObjects = (long) threadCount * objectsPerThread;
        double avgTimePerObject = (totalTime * 1000.0) / totalObjects; // 微秒
        
        System.out.println("   总耗时: " + totalTime + " ms");
        System.out.println("   总对象数: " + totalObjects);
        System.out.println("   平均分配时间: " + String.format("%.2f", avgTimePerObject) + " 微秒/对象");
        System.out.println("   吞吐量: " + (totalObjects / Math.max(totalTime, 1)) + " 对象/ms");
    }
    
    private static void showTLABJVMParameters() {
        System.out.println("\n4. TLAB相关JVM参数:");
        System.out.println("   基本参数:");
        System.out.println("   -XX:+UseTLAB          启用TLAB (默认)");
        System.out.println("   -XX:-UseTLAB          禁用TLAB");
        System.out.println();
        System.out.println("   大小控制:");
        System.out.println("   -XX:TLABSize=1024k    设置TLAB大小为1MB");
        System.out.println("   -XX:MinTLABSize=2k    最小TLAB大小");
        System.out.println("   -XX:MaxTLABSize=1024k 最大TLAB大小");
        System.out.println();
        System.out.println("   调优参数:");
        System.out.println("   -XX:TLABWasteTargetPercent=1  TLAB浪费目标(1-10%)");
        System.out.println("   -XX:TLABRefillWasteFraction=64 TLAB重填浪费比例");
        System.out.println("   -XX:-ResizeTLAB       禁用TLAB动态调整");
        System.out.println("   -XX:+PrintTLAB        打印TLAB统计信息");
        System.out.println();
        System.out.println("   监控参数:");
        System.out.println("   -XX:+UnlockDiagnosticVMOptions");
        System.out.println("   -XX:+PrintGCDetails");
        System.out.println("   -XX:+PrintGCApplicationStoppedTime");
    }
}