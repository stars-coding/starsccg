package com.stars.acm;

import java.util.Scanner;

/**
 * ACM ����ģ�壨����֮�ͣ�
 *
 * @author stars
 */
public class MainTemplate {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


            // ��ȡ�����Ԫ�ظ���
            int n = scanner.nextInt();
            // ��������
            int[] arr = new int[n];
            // ��ȡ����
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }
            // ��������Ԫ��֮��
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            // ��ӡ����Ԫ��֮��
            System.out.println("����֮�ͣ�" + sum);


        scanner.close();
    }
}
