#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
LeetCode 前 200 道题目爬取脚本
功能：获取题目描述并生成带 Javadoc 的 Java 方法签名文件
"""

import requests
import json
import os
import time
from pathlib import Path

# LeetCode GraphQL API 端点 - 使用中国站
LEETCODE_GRAPHQL_URL = "https://leetcode.cn/graphql"

# 请求头（模拟浏览器）
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
    "Content-Type": "application/json",
    "Referer": "https://leetcode.cn/",
}

# 输出目录
OUTPUT_DIR = Path("/Users/longyun/work/interview-prep/code.qoder")


def get_all_problems():
    """
    获取所有题目列表
    """
    query = """
    query problemsetQuestionListV2 {
        problemsetQuestionListV2 {
            questions {
                questionFrontendId
                titleSlug
                title
                translatedTitle
                paidOnly
            }
        }
    }
    """
    
    try:
        response = requests.post(
            LEETCODE_GRAPHQL_URL,
            json={"query": query},
            headers=HEADERS,
            timeout=30
        )
        
        if response.status_code == 200:
            data = response.json()
            if data.get("data") and data["data"].get("problemsetQuestionListV2"):
                return data["data"]["problemsetQuestionListV2"].get("questions", [])
        return []
    except Exception as e:
        print(f"获取题目列表失败：{e}")
        import traceback
        traceback.print_exc()
        return []


def get_problem_detail(title_slug):
    """
    根据题目 slug 获取详细信息
    """
    query = """
    query questionDetail($titleSlug: String!) {
        question(titleSlug: $titleSlug) {
            title
            translatedTitle
            content
            translatedContent
            difficulty
            categoryTitle
            codeSnippets {
                lang
                code
            }
            exampleTestcases
            hints
        }
    }
    """
    
    variables = {"titleSlug": title_slug}
    
    try:
        response = requests.post(
            LEETCODE_GRAPHQL_URL,
            json={"query": query, "variables": variables},
            headers=HEADERS,
            timeout=10
        )
        
        if response.status_code == 200:
            data = response.json()
            if data.get("data") and data["data"].get("question"):
                return data["data"]["question"]
        return None
    except Exception as e:
        print(f"获取题目详情失败：{e}")
        return None


def extract_java_signature(code_snippet, problem_title):
    """
    从代码片段中提取 Java 方法签名
    """
    if not code_snippet:
        # 如果没有代码片段，返回一个通用的方法签名
        return f"""
    /**
     * @Description {problem_title}
     * @param args 参数根据具体题目确定
     * @return 返回值根据具体题目确定
     */
    public static void solve(String[] args) {{
    }}
    """
    
    # 尝试从代码中提取类和方法
    lines = code_snippet.split('\n')
    method_lines = []
    in_method = False
    brace_count = 0
    
    for line in lines:
        stripped = line.strip()
        
        # 跳过空行和注释
        if not stripped or stripped.startswith('//'):
            continue
        
        # 查找方法声明
        if any(keyword in stripped for keyword in ['public', 'private', 'protected', 'static']):
            if '(' in stripped and ')' in stripped:
                in_method = True
                brace_count = stripped.count('{') - stripped.count('}')
                method_lines.append(line)
                continue
        
        if in_method:
            method_lines.append(line)
            brace_count += stripped.count('{') - stripped.count('}')
            
            if brace_count <= 0:
                break
    
    if method_lines:
        return '\n'.join(method_lines)
    
    # 如果无法解析，返回整个类定义
    return code_snippet


def generate_javadoc(problem, java_signature):
    """
    生成包含题目描述和方法签名的完整 Java文件内容
    """
    content = []
    
    # 文件头注释 - 题目描述
    content.append("/**")
    content.append(f" * 题目名称：{problem.get('translatedTitle') or problem.get('title', 'Unknown')}")
    
    if problem.get('title'):
        content.append(f" * 英文名称：{problem['title']}")
    
    content.append(f" * 难度：{problem.get('difficulty', 'Unknown')}")
    content.append(f" * 分类：{problem.get('categoryTitle', 'Unknown')}")
    content.append(" * ")
    
    # 提取题目描述（优先使用翻译版本）
    html_content = problem.get('translatedContent') or problem.get('content', '')
    # 简单的 HTML 标签清理
    import re
    clean_content = re.sub(r'<[^>]+>', '', html_content)
    clean_content = clean_content.replace('\n', ' ').strip()
    
    # 将长描述分段放入注释
    words = clean_content.split()
    current_line = " * 题目描述："
    for word in words:
        if len(current_line) + len(word) > 120:
            content.append(current_line)
            current_line = " *           " + word
        else:
            current_line += " " + word
    content.append(current_line)
    
    content.append(" * ")
    
    # 添加示例
    example_testcases = problem.get('exampleTestcases', '')
    if example_testcases:
        content.append(" * 示例:")
        for line in example_testcases.split('\n')[:5]:  # 限制示例行数
            content.append(f" *   {line}")
    
    content.append(" */")
    content.append("")
    
    # 添加类声明
    class_name = ''.join(word.capitalize() for word in (problem.get('translatedTitle') or problem.get('title', 'Solution')).split())
    content.append(f"public class {class_name} {{")
    content.append("")
    
    # 添加 Javadoc 方法签名
    content.append("    /**")
    content.append(f"     * @Description {problem.get('translatedTitle') or problem.get('title', 'Solve the problem')}")
    
    # 分析方法签名中的参数
    sig_lines = java_signature.split('\n')
    params_found = []
    return_type = "void"
    
    for line in sig_lines:
        # 查找参数
        param_match = re.search(r'\(([^)]*)\)', line)
        if param_match:
            params_str = param_match.group(1)
            if params_str.strip():
                params = params_str.split(',')
                for i, param in enumerate(params):
                    param = param.strip()
                    if param:
                        parts = param.split()
                        if len(parts) >= 2:
                            param_type = parts[0]
                            param_name = parts[-1].replace(';', '').strip()
                            params_found.append((param_type, param_name))
        
        # 查找返回值类型
        for return_kw in ['String', 'int', 'boolean', 'List', 'int[]', 'void']:
            if return_kw in line and line.strip().startswith(return_kw):
                return_type = return_kw
                break
        if 'public' in line and 'class' not in line:
            type_match = re.search(r'(?:public\s+)?(\w+(?:\[\])?)\s+\w+\s*\(', line)
            if type_match:
                return_type = type_match.group(1)
    
    # 添加 @Param 注释
    for param_type, param_name in params_found:
        content.append(f"     * @param {param_name} {param_type}类型的参数")
    
    if not params_found:
        content.append("     * @param 根据具体题目要求")
    
    # 添加 @Return 注释
    content.append(f"     * @return {return_type}类型的返回值")
    content.append("     */")
    
    # 添加方法签名（去掉方法体）
    for line in sig_lines:
        if line.strip():
            # 如果是方法声明行，去掉末尾的分号和大括号
            cleaned_line = line.rstrip(';').rstrip('{').strip()
            if cleaned_line:
                if not cleaned_line.endswith(')'):
                    cleaned_line = cleaned_line.rstrip('}')
                content.append(f"    {cleaned_line};")
            else:
                content.append(f"    {line}")
    
    content.append("}")
    
    return '\n'.join(content)


def create_paid_only_placeholder(question_id):
    """
    为付费题目创建占位文件
    """
    file_path = OUTPUT_DIR / f"{question_id}Code.java"
    content = """// 该题目为付费题目，暂无法获取内容
"""
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"  ✓ 付费题目：{question_id}Code.java (已标记)")
    return False


def process_question(question_id, problem_map):
    """
    处理单道题目
    """
    print(f"\n处理题目 #{question_id}...", end=" ")
    
    # 从映射中查找题目
    if question_id not in problem_map:
        print(f"  ✗ 题目不存在")
        # 创建空文件
        file_path = OUTPUT_DIR / f"{question_id}Code.java"
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(f"// 题目 #{question_id} 内容获取失败\n")
        return False
    
    problem_info = problem_map[question_id]
    title_slug = problem_info['titleSlug']
    is_paid = problem_info['isPaidOnly']
    
    if is_paid:
        create_paid_only_placeholder(question_id)
        return False
    
    # 获取题目详情
    problem = get_problem_detail(title_slug)
    
    if not problem:
        print(f"  ✗ 获取题目详情失败")
        file_path = OUTPUT_DIR / f"{question_id}Code.java"
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(f"// 题目 #{question_id} 详情获取失败\n")
        return False
    
    # 提取 Java 代码片段
    code_snippets = problem.get('codeSnippets', [])
    java_code = None
    for snippet in code_snippets:
        if snippet.get('lang') == 'Java':
            java_code = snippet.get('code')
            break
    
    # 生成完整的 Java文件
    java_content = generate_javadoc(problem, java_code)
    
    # 写入文件
    file_path = OUTPUT_DIR / f"{question_id}Code.java"
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(java_content)
    
    print(f"  ✓ 成功：{question_id}Code.java")
    return True


def main():
    """
    主函数
    """
    print("=" * 60)
    print("开始爬取 LeetCode 前 200 道题目")
    print("=" * 60)
    
    # 步骤 1: 获取所有题目列表
    print("\n正在获取题目列表...")
    all_problems = get_all_problems()
    
    if not all_problems:
        print("获取题目列表失败！")
        return
    
    print(f"成功获取 {len(all_problems)} 道题目")
    
    # 构建编号到题目的映射
    problem_map = {}
    for p in all_problems:
        qid_str = p.get('questionFrontendId', '0')
        try:
            qid = int(qid_str)
        except:
            continue
        problem_map[qid] = {
            'titleSlug': p.get('titleSlug'),
            'isPaidOnly': p.get('paidOnly', False),
            'title': p.get('translatedTitle') or p.get('title')
        }
    
    success_count = 0
    paid_count = 0
    error_count = 0
    
    # 步骤 2: 遍历前 200 道题目
    for question_id in range(1, 201):
        try:
            result = process_question(question_id, problem_map)
            if result:
                success_count += 1
            elif result is False:
                # 检查是否是付费题目
                file_path = OUTPUT_DIR / f"{question_id}Code.java"
                if file_path.exists():
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        if "付费题目" in content:
                            paid_count += 1
                        else:
                            error_count += 1
                else:
                    error_count += 1
            
            # 礼貌性延迟，避免请求过快
            time.sleep(0.3)
            
        except Exception as e:
            print(f"  ✗ 异常：{e}")
            error_count += 1
    
    # 输出统计报告
    print("\n" + "=" * 60)
    print("爬取完成！统计报告:")
    print("=" * 60)
    print(f"成功生成：{success_count} 题")
    print(f"付费题目：{paid_count} 题")
    print(f"获取失败：{error_count} 题")
    print(f"总计：{success_count + paid_count + error_count}/200 题")
    print(f"文件保存路径：{OUTPUT_DIR}")
    print("=" * 60)


if __name__ == "__main__":
    main()
