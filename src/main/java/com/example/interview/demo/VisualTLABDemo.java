package com.example.interview.demo;

/**
 * 最简单的TLAB可视化演示
 * 用最直观的方式展示TLAB的作用
 */
public class VisualTLABDemo {
    
    public static void main(String[] args) {
        System.out.println("=== TLAB可视化演示 ===\n");
        
        // 直接展示对比效果
        showTLABEffect();
        
        // 简单的面试回答模板
        showInterviewAnswer();
    }
    
    private static void showTLABEffect() {
        System.out.println("🎯 TLAB效果演示：");
        System.out.println();
        
        System.out.println("没有TLAB时（慢）：");
        System.out.println("线程1 → [等待] → 线程2 → [等待] → 线程3 → [等待] → 线程4");
        System.out.println("        ↓           ↓           ↓           ↓");
        System.out.println("     申请内存    申请内存    申请内存    申请内存");
        System.out.println("        ↓           ↓           ↓           ↓");
        System.out.println("      创建对象    创建对象    创建对象    创建对象");
        System.out.println();
        
        System.out.println("有TLAB时（快）：");
        System.out.println("线程1 → 线程2 → 线程3 → 线程4");
        System.out.println("  ↓       ↓       ↓       ↓");
        System.out.println("[TLAB]  [TLAB]  [TLAB]  [TLAB]");
        System.out.println("  ↓       ↓       ↓       ↓");
        System.out.println("创建对象  创建对象  创建对象  创建对象");
        System.out.println();
        
        System.out.println("简单理解：");
        System.out.println("• 每个线程都有自己的'小仓库'");
        System.out.println("• 从自己仓库拿东西，不用排队");
        System.out.println("• 结果就是速度更快，效率更高");
        System.out.println();
    }
    
    private static void showInterviewAnswer() {
        System.out.println("📋 面试标准回答：");
        System.out.println();
        System.out.println("问：什么是TLAB？");
        System.out.println("答：TLAB是Thread Local Allocation Buffer的缩写，");
        System.out.println("    中文叫'线程本地分配缓冲区'。");
        System.out.println();
        System.out.println("    简单说就是：给每个线程分配一个私人的小内存区域，");
        System.out.println("    线程创建对象时直接从自己的区域拿，不用和其他线程抢，");
        System.out.println("    这样就能大大提高多线程程序的运行速度。");
        System.out.println();
        System.out.println("    现在的JVM默认都开启了TLAB，我们基本不用管它，");
        System.out.println("    但在高并发场景下了解这个概念很有用。");
        System.out.println();
    }
}