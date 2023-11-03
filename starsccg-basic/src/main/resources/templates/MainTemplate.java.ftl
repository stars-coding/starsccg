package com.stars.acm;

import java.util.Scanner;

/**
 * ACM 输入模板（多数之和）
 *
 * @author ${author}
 */
public class MainTemplate {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

<#if loop>
        while (scanner.hasNext()) {
</#if>

            // 读取输入的元素个数
            int n = scanner.nextInt();
            // 创建数组
            int[] arr = new int[n];
            // 读取数组
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }
            // 计算数组元素之和
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            // 打印数组元素之和
            System.out.println("${outputText}" + sum);

<#if loop>
        }
</#if>

        scanner.close();
    }
}
