package com.example.interview.hash;

import java.util.*;

/**
 * LeetCode 1. 两数之和
 * 
 * 给定一个整数数组 nums 和一个整数目标值 target，
 * 请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
 * 
 * 示例：nums = [2,7,11,15], target = 9  →  返回 [0,1]
 */
public class TwoSum {

    // 解法1：返回索引 - 用 HashMap
    public int[] twoSumIndex(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }

    // 解法2：返回值 - 用 HashSet（更简洁）
    public int[] twoSum(int[] nums, int target) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) {
            if (set.contains(target - n)) {
                return new int[] { target - n, n };
            }
            set.add(n);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        TwoSum solution = new TwoSum();
        
        // 测试用例
        int[][] testCases = {
            {2, 7, 11, 15},  // target=9 → [0,1]
            {3, 2, 4},       // target=6 → [1,2]
            {3, 3}           // target=6 → [0,1]
        };
        int[] targets = {9, 6, 6};
        
        for (int i = 0; i < testCases.length; i++) {
            int[] result = solution.twoSum(testCases[i], targets[i]);
            System.out.printf("nums=%s, target=%d → %s%n",
                Arrays.toString(testCases[i]), 
                targets[i], 
                Arrays.toString(result));
        }
    }
}
