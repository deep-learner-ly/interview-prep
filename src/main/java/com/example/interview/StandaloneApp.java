package com.example.interview;

/**
 * 独立运行的应用程序 - 无需Spring依赖
 * 可以直接运行，避免IDE报错
 */
public class StandaloneApp {
    public static void main(String[] args) {
        System.out.println("=== 面试准备系统 ===");
        System.out.println("🚀 应用启动成功!");
        System.out.println("📚 可用功能:");
        System.out.println("  1. 二分查找算法");
        System.out.println("  2. 两数之和算法"); 
        System.out.println("  3. 深拷贝工具");
        System.out.println("");
        System.out.println("💡 运行算法演示:");
        System.out.println("   java com.example.interview.demo.AlgorithmDemo");
        System.out.println("");
        System.out.println("🎯 项目特点:");
        System.out.println("   • 纯Java实现，无外部依赖");
        System.out.println("   • 模块化设计，易于扩展");
        System.out.println("   • 包含完整的算法实现");
    }
}