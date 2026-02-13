package com.example.interview.demo;

/**
 * Java基本数据类型字节占用演示
 * 对应面试题：Java中基本数据类型占用的字节数？
 */
public class PrimitiveTypesDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Java基本数据类型字节占用演示 ===\n");
        
        // byte类型：1字节（-128~127）
        demonstrateByte();
        
        // short类型：2字节（-32768~32767）
        demonstrateShort();
        
        // int类型：4字节（-2^31~2^31-1）
        demonstrateInt();
        
        // long类型：8字节（-2^63~2^31-1）
        demonstrateLong();
        
        // float类型：4字节（32位IEEE 754标准）
        demonstrateFloat();
        
        // double类型：8字节（64位IEEE 754标准）
        demonstrateDouble();
        
        // char类型：2字节（Unicode字符）
        demonstrateChar();
        
        // boolean类型：JVM规范未定义大小
        demonstrateBoolean();
        
        // 内存占用总结
        printMemorySummary();
    }
    
    private static void demonstrateByte() {
        System.out.println("1. byte类型:");
        System.out.println("   • 占用字节数: " + Byte.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Byte.MIN_VALUE + " ~ " + Byte.MAX_VALUE);
        System.out.println("   • 默认值: " + (byte)0);
        System.out.println("   • 应用场景: 节省内存的整数存储");
        System.out.println();
    }
    
    private static void demonstrateShort() {
        System.out.println("2. short类型:");
        System.out.println("   • 占用字节数: " + Short.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Short.MIN_VALUE + " ~ " + Short.MAX_VALUE);
        System.out.println("   • 默认值: " + (short)0);
        System.out.println("   • 应用场景: 数组索引、小范围整数");
        System.out.println();
    }
    
    private static void demonstrateInt() {
        System.out.println("3. int类型:");
        System.out.println("   • 占用字节数: " + Integer.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Integer.MIN_VALUE + " ~ " + Integer.MAX_VALUE);
        System.out.println("   • 默认值: " + 0);
        System.out.println("   • 应用场景: 最常用的整数类型");
        System.out.println();
    }
    
    private static void demonstrateLong() {
        System.out.println("4. long类型:");
        System.out.println("   • 占用字节数: " + Long.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Long.MIN_VALUE + " ~ " + Long.MAX_VALUE);
        System.out.println("   • 默认值: " + 0L);
        System.out.println("   • 应用场景: 大整数计算、时间戳");
        System.out.println();
    }
    
    private static void demonstrateFloat() {
        System.out.println("5. float类型:");
        System.out.println("   • 占用字节数: " + Float.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Float.MIN_VALUE + " ~ " + Float.MAX_VALUE);
        System.out.println("   • 精度: 6-7位有效数字");
        System.out.println("   • 默认值: " + 0.0f);
        System.out.println("   • 应用场景: 单精度浮点计算");
        System.out.println();
    }
    
    private static void demonstrateDouble() {
        System.out.println("6. double类型:");
        System.out.println("   • 占用字节数: " + Double.BYTES + " 字节");
        System.out.println("   • 取值范围: " + Double.MIN_VALUE + " ~ " + Double.MAX_VALUE);
        System.out.println("   • 精度: 15位有效数字");
        System.out.println("   • 默认值: " + 0.0);
        System.out.println("   • 应用场景: 双精度浮点计算（推荐）");
        System.out.println();
    }
    
    private static void demonstrateChar() {
        System.out.println("7. char类型:");
        System.out.println("   • 占用字节数: " + Character.BYTES + " 字节");
        System.out.println("   • 取值范围: '\\u0000' ~ '\\uffff' (0~65535)");
        System.out.println("   • 默认值: '\\u0000'");
        System.out.println("   • 应用场景: Unicode字符存储");
        System.out.println();
    }
    
    private static void demonstrateBoolean() {
        System.out.println("8. boolean类型:");
        System.out.println("   • JVM规范未明确定义字节大小");
        System.out.println("   • 实际实现：通常1位或1字节");
        System.out.println("   • 取值范围: true 或 false");
        System.out.println("   • 默认值: false");
        System.out.println("   • 应用场景: 逻辑判断、开关控制");
        System.out.println();
    }
    
    private static void printMemorySummary() {
        System.out.println("=== 内存占用总结 ===");
        System.out.println("总字节数统计:");
        long totalBytes = Byte.BYTES + Short.BYTES + Integer.BYTES + 
                         Long.BYTES + Float.BYTES + Double.BYTES + 
                         Character.BYTES;
        System.out.println("• 数值类型总和: " + totalBytes + " 字节");
        System.out.println("• 加上boolean: 约 " + (totalBytes + 1) + " 字节");
        System.out.println();
        
        System.out.println("=== 性能建议 ===");
        System.out.println("1. 优先使用int而非short/byte（现代CPU优化）");
        System.out.println("2. 浮点运算优先使用double（精度更高）");
        System.out.println("3. 大量数据存储时考虑使用较小类型节省内存");
        System.out.println("4. boolean数组在某些JVM实现中会被优化打包存储");
    }
}