package com.example.interview.array;

/**
 * 二分查找经典题目集合 - Java实现
 * 
 * 包含以下经典二分查找问题：
 * 1. 基础二分查找
 * 2. 寻找峰值元素  
 * 3. 在旋转排序数组中搜索
 * 4. 寻找插入位置
 * 5. 搜索二维矩阵
 */
public class BinarySearchProblems {
    
    /**
     * 基础二分查找 - 迭代版本
     * 在有序数组中查找目标值的索引
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    public static int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    /**
     * 基础二分查找 - 递归版本
     * 在有序数组中查找目标值的索引
     * 时间复杂度: O(log n)
     * 空间复杂度: O(log n) - 递归调用栈
     */
    public static int binarySearchRecursive(int[] nums, int target) {
        return binarySearchHelper(nums, target, 0, nums.length - 1);
    }
    
    private static int binarySearchHelper(int[] nums, int target, int left, int right) {
        // 递归终止条件
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            // 在右半部分继续查找
            return binarySearchHelper(nums, target, mid + 1, right);
        } else {
            // 在左半部分继续查找
            return binarySearchHelper(nums, target, left, mid - 1);
        }
    }
    
    /**
     * 寻找峰值元素
     * 峰值元素是指其值严格大于左右相邻值的元素
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    public static int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 如果中间元素小于右邻居，峰值在右半部分
            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                // 否则峰值在左半部分（包括mid）
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * 在旋转排序数组中搜索
     * 数组原本是升序排列，经过旋转后查找目标值
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    public static int searchInRotatedArray(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断哪一半是有序的
            if (nums[left] <= nums[mid]) { // 左半部分有序
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else { // 右半部分有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 寻找插入位置
     * 在有序数组中找到目标值应该插入的位置
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    public static int searchInsertPosition(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left; // 返回插入位置
    }
    
    /**
     * 搜索二维矩阵
     * 矩阵每行从左到右递增，每列从上到下递增
     * 时间复杂度: O(log(m*n))
     * 空间复杂度: O(1)
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int left = 0;
        int right = rows * cols - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midValue = matrix[mid / cols][mid % cols];
            
            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 二分查找经典题目测试 ===\n");
        
        // 测试基础二分查找
        int[] arr1 = {1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println("基础二分查找:");
        System.out.println("数组: [1, 3, 5, 7, 9, 11, 13, 15]");
        System.out.println("迭代版本查找 7: " + binarySearch(arr1, 7));
        System.out.println("迭代版本查找 6: " + binarySearch(arr1, 6));
        System.out.println("递归版本查找 7: " + binarySearchRecursive(arr1, 7));
        System.out.println("递归版本查找 6: " + binarySearchRecursive(arr1, 6));
        System.out.println();
        
        // 测试寻找峰值元素
        System.out.println("寻找峰值元素:");
        
        // 测试用例1：基本情况
        int[] arr2_1 = {1, 2, 3, 1};
        System.out.println("测试1 - 基本情况:");
        System.out.println("数组: [1, 2, 3, 1]");
        System.out.println("峰值索引: " + findPeakElement(arr2_1));
        System.out.println("峰值值: " + arr2_1[findPeakElement(arr2_1)]);
        
        // 测试用例2：单调递增
        int[] arr2_2 = {1, 2, 3, 4, 5};
        System.out.println("\n测试2 - 单调递增:");
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("峰值索引: " + findPeakElement(arr2_2));
        System.out.println("峰值值: " + arr2_2[findPeakElement(arr2_2)]);
        
        // 测试用例3：单调递减
        int[] arr2_3 = {5, 4, 3, 2, 1};
        System.out.println("\n测试3 - 单调递减:");
        System.out.println("数组: [5, 4, 3, 2, 1]");
        System.out.println("峰值索引: " + findPeakElement(arr2_3));
        System.out.println("峰值值: " + arr2_3[findPeakElement(arr2_3)]);
        
        // 测试用例4：多个峰值
        int[] arr2_4 = {1, 2, 1, 3, 5, 6, 4};
        System.out.println("\n测试4 - 多个峰值:");
        System.out.println("数组: [1, 2, 1, 3, 5, 6, 4]");
        System.out.println("峰值索引: " + findPeakElement(arr2_4));
        System.out.println("峰值值: " + arr2_4[findPeakElement(arr2_4)]);
        
        // 测试用例5：单个元素
        int[] arr2_5 = {42};
        System.out.println("\n测试5 - 单个元素:");
        System.out.println("数组: [42]");
        System.out.println("峰值索引: " + findPeakElement(arr2_5));
        System.out.println("峰值值: " + arr2_5[findPeakElement(arr2_5)]);
        
        // 测试用例6：两个元素
        int[] arr2_6 = {1, 2};
        System.out.println("\n测试6 - 两个元素:");
        System.out.println("数组: [1, 2]");
        System.out.println("峰值索引: " + findPeakElement(arr2_6));
        System.out.println("峰值值: " + arr2_6[findPeakElement(arr2_6)]);
        
        // 测试用例7：平台峰值
        int[] arr2_7 = {1, 2, 2, 2, 1};
        System.out.println("\n测试7 - 平台峰值:");
        System.out.println("数组: [1, 2, 2, 2, 1]");
        System.out.println("峰值索引: " + findPeakElement(arr2_7));
        System.out.println("峰值值: " + arr2_7[findPeakElement(arr2_7)]);
        
        System.out.println();
        
        // 测试旋转数组搜索
        int[] arr3 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("旋转数组搜索:");
        System.out.println("数组: [4, 5, 6, 7, 0, 1, 2]");
        System.out.println("查找 0: " + searchInRotatedArray(arr3, 0));
        System.out.println("查找 3: " + searchInRotatedArray(arr3, 3));
        System.out.println();
        
        // 测试插入位置
        int[] arr4 = {1, 3, 5, 6};
        System.out.println("寻找插入位置:");
        System.out.println("数组: [1, 3, 5, 6]");
        System.out.println("插入 5: " + searchInsertPosition(arr4, 5));
        System.out.println("插入 2: " + searchInsertPosition(arr4, 2));
        System.out.println("插入 7: " + searchInsertPosition(arr4, 7));
        System.out.println();
        
        // 测试二维矩阵搜索
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        System.out.println("二维矩阵搜索:");
        System.out.println("查找 5: " + searchMatrix(matrix, 5));
        System.out.println("查找 15: " + searchMatrix(matrix, 15));
        System.out.println("查找 30: " + searchMatrix(matrix, 30));
    }
}