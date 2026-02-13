package com.example.interview.demo;

/**
 * Java try-catch-finally 执行顺序演示
 * 对应面试题：Java中try-catch-finally的执行顺序？
 */
public class TryCatchFinallyDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Java try-catch-finally 执行顺序演示 ===\n");
        
        // 情况1：正常执行（无异常）
        demonstrateNormalExecution();
        
        // 情况2：出现异常被catch处理
        demonstrateExceptionHandled();
        
        // 情况3：出现异常未被catch处理
        try {
            demonstrateExceptionUnhandled();
        } catch (Exception e) {
            System.out.println("   → 捕获到未处理的异常: " + e.getClass().getSimpleName() + "\n");
        }
        
        // 情况4：try中有return
        System.out.println("情况4结果: " + demonstrateTryReturn());
        System.out.println();
        
        // 情况5：catch中有return
        System.out.println("情况5结果: " + demonstrateCatchReturn());
        System.out.println();
        
        // 情况6：finally中修改引用类型内容
        StringBuilder sb = demonstrateFinallyModifyReturnValue();
        System.out.println("情况6最终结果: " + sb.toString());
        System.out.println();
        
        // 总结
        printBestPractices();
    }
    
    /**
     * 情况1：正常执行（无异常）
     * 执行顺序：try → finally → 正常结束
     */
    private static void demonstrateNormalExecution() {
        System.out.println("1. 正常执行情况（无异常）:");
        try {
            System.out.println("   → 执行 try 块");
        } catch (Exception e) {
            System.out.println("   → 执行 catch 块");
        } finally {
            System.out.println("   → 执行 finally 块");
        }
        System.out.println("   → 方法正常结束\n");
    }
    
    /**
     * 情况2：出现异常被catch处理
     * 执行顺序：try → catch → finally → 异常处理结束
     */
    private static void demonstrateExceptionHandled() {
        System.out.println("2. 异常被捕获处理:");
        try {
            System.out.println("   → 执行 try 块");
            throw new RuntimeException("模拟异常");
        } catch (RuntimeException e) {
            System.out.println("   → 执行 catch 块，捕获异常: " + e.getMessage());
        } finally {
            System.out.println("   → 执行 finally 块");
        }
        System.out.println("   → 异常处理完毕，方法正常结束\n");
    }
    
    /**
     * 情况3：出现异常未被catch处理
     * 执行顺序：try → finally → 抛出异常
     */
    private static void demonstrateExceptionUnhandled() {
        System.out.println("3. 异常未被捕获:");
        try {
            System.out.println("   → 执行 try 块");
            throw new IllegalArgumentException("未被捕获的异常");
        } catch (IllegalStateException e) {  // 故意使用不匹配的异常类型
            System.out.println("   → 执行 catch 块（这个不会被执行）");
        } finally {
            System.out.println("   → 执行 finally 块（即使异常未被捕获也会执行）");
        }
        // 注意：这里会抛出异常，下面的代码不会执行
        System.out.println("   → 这行代码不会被执行\n");
    }
    
    /**
     * 情况4：try中有return
     * 执行顺序：执行return表达式 → finally → return
     */
    private static int demonstrateTryReturn() {
        System.out.println("4. try中有return语句:");
        try {
            System.out.println("   → 执行 try 块");
            System.out.println("   → 准备return 42");
            return 42;  // 先计算返回值42，暂存
        } catch (Exception e) {
            System.out.println("   → 执行 catch 块");
            return -1;
        } finally {
            System.out.println("   → 执行 finally 块（在return之前执行）");
            // finally中的代码会执行，但不会影响已暂存的返回值
        }
        // 这行不会被执行
    }
    
    /**
     * 情况5：catch中有return
     * 执行顺序：try → catch执行return表达式 → finally → return
     */
    private static int demonstrateCatchReturn() {
        System.out.println("5. catch中有return语句:");
        try {
            System.out.println("   → 执行 try 块");
            throw new Exception("触发catch");
        } catch (Exception e) {
            System.out.println("   → 执行 catch 块");
            System.out.println("   → 准备return 100");
            return 100;  // 先计算返回值100，暂存
        } finally {
            System.out.println("   → 执行 finally 块（在catch的return之前执行）");
        }
    }
    
    /**
     * 情况6：finally中修改引用类型的"返回值"
     * 注意：对于基本类型，finally无法修改已暂存的返回值
     * 但对于引用类型，可以修改对象内容
     */
    private static StringBuilder demonstrateFinallyModifyReturnValue() {
        System.out.println("7. finally中修改引用类型内容:");
        StringBuilder result = new StringBuilder("初始值");
        
        try {
            System.out.println("   → 执行 try 块");
            result.append("-try");
            System.out.println("   → 准备返回: " + result.toString());
            return result;  // 返回StringBuilder引用
        } finally {
            System.out.println("   → 执行 finally 块");
            result.append("-finally");  // 修改同一个对象的内容
            System.out.println("   → finally中修改后: " + result.toString());
            // 虽然修改了对象内容，但返回的仍然是同一个对象引用
        }
    }
    
    /**
     * 输出最佳实践总结
     */
    private static void printBestPractices() {
        System.out.println("=== try-catch-finally 最佳实践 ===");
        System.out.println("1. finally块总会执行（除非JVM退出）");
        System.out.println("2. return语句的执行顺序：先计算返回值→执行finally→真正返回");
        System.out.println("3. finally中的return会覆盖try/catch中的return（不推荐）");
        System.out.println("4. finally适合做资源清理工作");
        System.out.println("5. 避免在finally中使用return或抛出异常");
        System.out.println("6. 对于引用类型，finally可以修改对象内容但不能改变引用");
    }
}