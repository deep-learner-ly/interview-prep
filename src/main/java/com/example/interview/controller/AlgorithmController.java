package com.example.interview.controller;

import com.example.interview.array.BinarySearchProblems;
import com.example.interview.hash.TwoSum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AlgorithmController {

    @GetMapping("/binary-search")
    public Map<String, Object> binarySearchDemo(
            @RequestParam(defaultValue = "1,3,5,7,9,11,13,15") String array,
            @RequestParam(defaultValue = "7") int target) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 解析输入数组
            int[] nums = Arrays.stream(array.split(","))
                              .mapToInt(Integer::parseInt)
                              .toArray();
            
            BinarySearchProblems bs = new BinarySearchProblems();
            int index = bs.binarySearch(nums, target);
            
            result.put("success", true);
            result.put("array", Arrays.toString(nums));
            result.put("target", target);
            result.put("foundIndex", index);
            result.put("foundValue", index != -1 ? nums[index] : null);
            result.put("message", index != -1 ? "找到目标值" : "未找到目标值");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/two-sum")
    public Map<String, Object> twoSumDemo(
            @RequestParam(defaultValue = "2,7,11,15") String array,
            @RequestParam(defaultValue = "9") int target) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            int[] nums = Arrays.stream(array.split(","))
                              .mapToInt(Integer::parseInt)
                              .toArray();
            
            TwoSum twoSum = new TwoSum();
            int[] indices = twoSum.twoSum(nums, target);
            
            result.put("success", true);
            result.put("array", Arrays.toString(nums));
            result.put("target", target);
            result.put("indices", Arrays.toString(indices));
            result.put("values", indices.length == 2 ? 
                "[" + nums[indices[0]] + ", " + nums[indices[1]] + "]" : "[]");
            result.put("message", indices.length == 2 ? "找到两个数" : "未找到符合条件的两个数");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "Interview Preparation System");
        result.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return result;
    }
}