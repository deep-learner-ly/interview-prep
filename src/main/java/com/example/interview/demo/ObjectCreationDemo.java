package com.example.interview.demo;

/**
 * Java对象创建过程演示程序
 * 详细展示对象从类加载到创建完成的完整过程
 * 
 * 面试题48参考：https://www.baeldung.com/java-object-creation
 */
public class ObjectCreationDemo {
    
    // 演示用的测试类
    static class Student {
        private String name;
        private int age;
        private String school;
        
        // 静态代码块 - 类加载时执行
        static {
            System.out.println("  >> Student类静态代码块执行 - 类加载阶段");
        }
        
        // 实例代码块 - 对象创建时执行
        {
            System.out.println("  >> Student实例代码块执行 - 对象初始化阶段");
        }
        
        // 构造方法
        public Student() {
            System.out.println("  >> Student无参构造方法执行");
            this.name = "默认姓名";
            this.age = 18;
            this.school = "默认学校";
        }
        
        public Student(String name, int age) {
            System.out.println("  >> Student有参构造方法执行");
            this.name = name;
            this.age = age;
            this.school = "默认学校";
        }
        
        public Student(String name, int age, String school) {
            System.out.println("  >> Student完整构造方法执行");
            this.name = name;
            this.age = age;
            this.school = school;
        }
        
        @Override
        public String toString() {
            return "Student{name='" + name + "', age=" + age + ", school='" + school + "'}";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Java对象创建过程详细演示 ===\n");
        
        // 1. 类加载检查演示
        demonstrateClassLoading();
        
        // 2. 内存分配演示
        demonstrateMemoryAllocation();
        
        // 3. 内存初始化演示
        demonstrateMemoryInitialization();
        
        // 4. 对象头设置演示
        demonstrateObjectHeader();
        
        // 5. <init>方法执行演示
        demonstrateInitMethod();
        
        // 6. 完整创建过程演示
        demonstrateCompleteProcess();
        
        System.out.println("\n=== 演示完成 ===");
    }
    
    /**
     * 1. 类加载检查演示
     */
    private static void demonstrateClassLoading() {
        System.out.println("1. 类加载检查阶段:");
        System.out.println("   - JVM检查Student类是否已加载");
        System.out.println("   - 如果未加载，则执行类加载过程");
        System.out.println("   - 包括：加载.class文件、验证、准备、解析、初始化");
        
        // 触发类加载
        System.out.println("   - 创建第一个Student对象，触发类加载:");
        Student student1 = new Student();
        System.out.println("   - 创建完成: " + student1);
        System.out.println();
    }
    
    /**
     * 2. 内存分配演示
     */
    private static void demonstrateMemoryAllocation() {
        System.out.println("2. 内存分配阶段:");
        System.out.println("   - 在Eden区为新对象分配内存空间");
        System.out.println("   - 分配方式：指针碰撞（内存规整）或空闲列表（内存碎片）");
        System.out.println("   - TLAB优化：线程本地分配缓冲区提高分配效率");
        
        System.out.println("   - 分配不同大小的对象:");
        Student student2 = new Student("张三", 20);
        System.out.println("   - 小对象分配完成: " + student2);
        
        // 创建较大的对象来演示内存分配
        StringBuilder largeData = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeData.append("data");
        }
        System.out.println("   - 大对象分配演示完成");
        System.out.println();
    }
    
    /**
     * 3. 内存初始化演示
     */
    private static void demonstrateMemoryInitialization() {
        System.out.println("3. 内存初始化阶段:");
        System.out.println("   - 将分配的内存空间初始化为零值");
        System.out.println("   - 基本类型：int=0, double=0.0, boolean=false");
        System.out.println("   - 引用类型：null");
        System.out.println("   - 注意：不包括对象头信息");
        
        // 通过反射查看初始化状态
        System.out.println("   - 创建对象观察初始化过程:");
        Student student3 = new Student();
        System.out.println("   - 初始化后对象状态: " + student3);
        System.out.println();
    }
    
    /**
     * 4. 对象头设置演示
     */
    private static void demonstrateObjectHeader() {
        System.out.println("4. 对象头设置阶段:");
        System.out.println("   - 设置对象的元数据信息");
        System.out.println("   - 包括：哈希码、GC分代年龄、锁状态标志");
        System.out.println("   - 类型指针：指向对象所属的类元数据");
        System.out.println("   - 数组长度：如果是数组对象，记录数组长度");
        
        Student student4 = new Student("李四", 22, "清华大学");
        System.out.println("   - 对象创建完成: " + student4);
        
        // 演示对象头信息（通过hashCode间接体现）
        System.out.println("   - 对象哈希码: " + student4.hashCode());
        System.out.println("   - 对象头信息已设置完成");
        System.out.println();
    }
    
    /**
     * 5. <init>方法执行演示
     */
    private static void demonstrateInitMethod() {
        System.out.println("5. <init>方法执行阶段:");
        System.out.println("   - 执行实例初始化代码块");
        System.out.println("   - 执行构造方法");
        System.out.println("   - 按程序员意愿初始化对象属性");
        System.out.println("   - 调用顺序：父类构造器 → 实例代码块 → 构造方法");
        
        System.out.println("   - 执行不同构造方法:");
        Student student5 = new Student();  // 无参构造
        System.out.println("   - 无参构造结果: " + student5);
        
        Student student6 = new Student("王五", 21);  // 有参构造
        System.out.println("   - 有参构造结果: " + student6);
        
        Student student7 = new Student("赵六", 23, "北京大学");  // 完整构造
        System.out.println("   - 完整构造结果: " + student7);
        System.out.println();
    }
    
    /**
     * 6. 完整创建过程演示
     */
    private static void demonstrateCompleteProcess() {
        System.out.println("6. 完整对象创建过程演示:");
        System.out.println("   按顺序执行以下步骤：");
        System.out.println("   ① 类加载检查 → ② 内存分配 → ③ 内存初始化");
        System.out.println("   ④ 对象头设置 → ⑤ <init>方法执行 → ⑥ 返回对象引用");
        
        System.out.println("\n   开始创建Student对象:");
        System.out.println("   =====================================");
        Student student8 = new Student("完整流程测试", 25, "演示大学");
        System.out.println("   =====================================");
        System.out.println("   对象创建完成，返回引用: " + student8);
        
        // 演示对象创建后的使用
        System.out.println("\n   创建后对象的使用:");
        System.out.println("   - 访问属性: " + student8.toString());
        System.out.println("   - 调用方法: hashCode=" + student8.hashCode());
        System.out.println("   - 对象状态: 已完全初始化，可以正常使用");
    }
}