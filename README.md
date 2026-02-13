# Java面试准备系统

这是一个基于Spring Boot的Java算法面试题准备系统，包含了常用的算法实现和RESTful API接口。

## 📁 项目结构

```
interview-prep/
├── pom.xml                           # Maven配置文件
├── src/
│   └── main/
│       ├── java/com/example/interview/
│       │   ├── InterviewPrepApplication.java    # Spring Boot启动类
│       │   ├── controller/
│       │   │   └── AlgorithmController.java     # REST API控制器
│       │   ├── array/
│       │   │   └── BinarySearchProblems.java    # 二分查找算法
│       │   └── hash/
│       │       └── TwoSum.java                  # 两数之和算法
│       └── resources/
│           └── application.properties           # 配置文件
└── project-info.sh                   # 项目信息脚本
```

## 🚀 快速开始

### 1. 环境要求
- JDK 11 或更高版本
- Maven 3.6+ (可选，用于构建项目)

### 2. 构建和运行

```bash
# 编译项目
mvn clean compile

# 启动Spring Boot应用
mvn spring-boot:run

# 或者打包后运行
mvn package
java -jar target/interview-prep-1.0.0.jar
```

### 3. 访问API

应用启动后，默认端口为8080：

- **健康检查**: `GET http://localhost:8080/api/health`
- **二分查找**: `GET http://localhost:8080/api/binary-search?array=1,3,5,7,9&target=5`
- **两数之和**: `GET http://localhost:8080/api/two-sum?array=2,7,11,15&target=9`

## 🧠 算法功能

### 二分查找 (Binary Search)
```java
// 使用示例
BinarySearchProblems bs = new BinarySearchProblems();
int[] arr = {1, 3, 5, 7, 9};
int index = bs.binarySearch(arr, 5); // 返回索引2
```

### 两数之和 (Two Sum)
```java
// 使用示例
TwoSum ts = new TwoSum();
int[] nums = {2, 7, 11, 15};
int[] result = ts.twoSum(nums, 9); // 返回[0, 1]
```

## 🔧 技术栈

- **Spring Boot 2.7.0** - 应用框架
- **Apache Commons Lang 3.12.0** - 工具类库
- **Jackson 2.13.3** - JSON处理
- **JUnit 5** - 单元测试

## 📦 Maven依赖

项目已配置以下核心依赖：
- `spring-boot-starter` - Spring Boot核心
- `spring-boot-starter-web` - Web支持
- `commons-lang3` - Apache工具类
- `jackson-databind` - JSON序列化
- `spring-boot-starter-test` - 测试支持

## 🎯 特色功能

1. **零配置启动** - 开箱即用的Spring Boot应用
2. **RESTful API** - 通过HTTP接口调用算法
3. **模块化设计** - 算法按类别组织
4. **易于扩展** - 可轻松添加新的算法实现
5. **深拷贝工具** - 内置高效的深拷贝解决方案

## 📚 学习资源

这个项目可以帮助你：
- 掌握常见的面试算法
- 理解Spring Boot开发模式
- 学习RESTful API设计
- 实践Java最佳编程实践

---
*Happy Coding! 🚀*