package com.example.interview.demo;

/**
 * Java对象创建过程 - 面试精简版演示
 * 突出面试重点，便于记忆和理解
 */
public class SimpleObjectCreationDemo {
    
    static class InterviewCandidate {
        private String name;
        private int experience;
        
        // 面试重点：构造方法执行顺序
        public InterviewCandidate() {
            System.out.println("   → 执行构造方法");
            this.name = "候选人";
            this.experience = 0;
        }
        
        @Override
        public String toString() {
            return "InterviewCandidate{name='" + name + "', experience=" + experience + "}";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Java对象创建过程（面试版）===\n");
        
        // 面试标准回答演示
        showInterviewProcess();
        
        // 关键知识点总结
        showKeyPoints();
    }
    
    private static void showInterviewProcess() {
        System.out.println("🎯 面试标准回答 - 对象创建6个步骤：");
        System.out.println();
        System.out.println("1. 类加载检查");
        System.out.println("   → 检查类是否已加载，未加载则执行类加载");
        System.out.println();
        System.out.println("2. 内存分配"); 
        System.out.println("   → 在Eden区分配内存（使用TLAB优化）");
        System.out.println();
        System.out.println("3. 内存初始化");
        System.out.println("   → 将内存空间初始化为零值（int=0,引用=null等）");
        System.out.println();
        System.out.println("4. 设置对象头");
        System.out.println("   → 设置哈希码、GC年龄、锁状态等元数据");
        System.out.println();
        System.out.println("5. 执行<init>方法");
        System.out.println("   → 按顺序：父类构造器 → 实例代码块 → 构造方法");
        System.out.println();
        System.out.println("6. 返回对象引用");
        System.out.println("   → 对象创建完成，可以使用");
        System.out.println();
        
        System.out.println("🚀 实际创建演示：");
        System.out.println("=====================================");
        InterviewCandidate candidate = new InterviewCandidate();
        System.out.println("=====================================");
        System.out.println("创建完成：" + candidate);
        System.out.println("对象引用地址：" + candidate.hashCode());
        System.out.println();
    }
    
    private static void showKeyPoints() {
        System.out.println("📋 面试重点记忆：");
        System.out.println();
        System.out.println("✅ 必考知识点：");
        System.out.println("• 类加载时机：第一次使用类时触发");
        System.out.println("• 内存分配区域：Eden区（新生代）");
        System.out.println("• 初始化内容：零值初始化（不包括对象头）");
        System.out.println("• 构造执行顺序：父类→代码块→构造方法");
        System.out.println("• TLAB作用：提高多线程对象分配效率");
        System.out.println();
        
        System.out.println("💡 面试技巧：");
        System.out.println("• 用'检查→分配→初始化→设置→执行→返回'的顺序记忆");
        System.out.println("• 强调TLAB在内存分配阶段的优化作用");
        System.out.println("• 说明<init>方法的执行顺序很重要");
        System.out.println();
    }
}