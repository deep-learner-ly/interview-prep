package com.example.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterviewPrepApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewPrepApplication.class, args);
        System.out.println("=== 面试准备系统启动成功 ===");
        System.out.println("访问地址: http://localhost:8080");
    }
}