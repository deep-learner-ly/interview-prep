// 包路径: src/main/java/com/example/interview/demo/
package com.example.interview.demo;

import com.example.interview.array.BinarySearchProblems;
import com.example.interview.hash.TwoSum;

/**
 * 算法演示类 - 无需Spring依赖即可运行
 * 用于验证算法功能和消除IDE报错
 */
public class AlgorithmDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 算法功能演示 ===\n");
        
        // 演示二分查找
        demoBinarySearch();
        
        System.out.println();
        
        // 演示两数之和
        demoTwoSum();
        
        System.out.println("\n=== 深拷贝工具演示 ===");
        demoDeepCopy();
    }
    
    private static void demoBinarySearch() {
        System.out.println("1. 二分查找算法演示:");
        BinarySearchProblems bs = new BinarySearchProblems();
        
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
        int target = 7;
        
        int result = bs.binarySearch(arr, target);
        
        System.out.println("数组: " + java.util.Arrays.toString(arr));
        System.out.println("查找目标: " + target);
        System.out.println("结果索引: " + result);
        System.out.println("找到的值: " + (result != -1 ? arr[result] : "未找到"));
    }
    
    private static void demoTwoSum() {
        System.out.println("2. 两数之和算法演示:");
        TwoSum ts = new TwoSum();
        
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        
        int[] result = ts.twoSumIndex(nums, target);
        
        System.out.println("数组: " + java.util.Arrays.toString(nums));
        System.out.println("目标和: " + target);
        if (result.length == 2) {
            System.out.println("找到索引: [" + result[0] + ", " + result[1] + "]");
            System.out.println("对应值: [" + nums[result[0]] + ", " + nums[result[1]] + "]");
        } else {
            System.out.println("未找到符合条件的两个数");
        }
    }
    
    private static void demoDeepCopy() {
        System.out.println("3. 深拷贝工具演示:");
        
        // 创建测试对象
        Person original = new Person("张三", 30);
        java.util.List<Address> addresses = new java.util.ArrayList<>();
        addresses.add(new Address("北京", "长安街1号"));
        addresses.add(new Address("上海", "南京路2号"));
        original.setAddresses(addresses);
        
        // 使用深拷贝工具
        Person copy = DeepCloneUtil.cloneBySerialization(original);
        
        System.out.println("原始对象ID: " + System.identityHashCode(original));
        System.out.println("拷贝对象ID: " + System.identityHashCode(copy));
        System.out.println("是否同一对象: " + (original == copy ? "是" : "否"));
        
        if (original.getAddresses() != null && copy.getAddresses() != null) {
            System.out.println("原始List ID: " + System.identityHashCode(original.getAddresses()));
            System.out.println("拷贝List ID: " + System.identityHashCode(copy.getAddresses()));
            System.out.println("List是否同一对象: " + (original.getAddresses() == copy.getAddresses() ? "是" : "否"));
        }
    }
}

// 简化的深拷贝工具类
class DeepCloneUtil {
    public static <T extends java.io.Serializable> T cloneBySerialization(T obj) {
        if (obj == null) return null;
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
            @SuppressWarnings("unchecked")
            T cloned = (T) ois.readObject();
            ois.close();
            return cloned;
        } catch (Exception e) {
            throw new RuntimeException("深拷贝失败", e);
        }
    }
}

// 测试用的实体类
class Person implements java.io.Serializable {
    private String name;
    private int age;
    private java.util.List<Address> addresses;
    
    public Person() {}
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public java.util.List<Address> getAddresses() { return addresses; }
    public void setAddresses(java.util.List<Address> addresses) { this.addresses = addresses; }
}

class Address implements java.io.Serializable {
    private String city;
    private String street;
    
    public Address() {}
    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
}