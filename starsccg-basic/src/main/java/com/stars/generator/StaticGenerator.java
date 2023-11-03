package com.stars.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 静态文件生成器
 *
 * @author stars
 * @version 1.0
 */
public class StaticGenerator {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) {

        /*
        // 顶级项目的根路径
        String projectPath = System.getProperty("user.dir");
        // 输入路径（动态获取系统路径分隔符）-绝对路径
        String inputPath = projectPath + File.separator + "starsccg-demo-projects" + File.separator + "acm-template";
        */

        // 模块单独打开时，为当前模块根路径
        String projectPath = System.getProperty("user.dir");
        // 顶级项目的根路径
        File parentFile = new File(projectPath).getParentFile();
        // 输入路径（动态获取系统路径分隔符）-绝对路径
        String inputPath = new File(parentFile, "starsccg-demo-projects/acm-template").getAbsolutePath();
        // 输出路径-绝对路径
        String outputPath = projectPath;
        // 复制
//        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
        StaticGenerator.copyFilesByRecursive(inputPath, outputPath);
    }

    /**
     * 胡图拷贝文件（通过 Hutool 工具类，将输入目录完整的拷贝到输出目录下）
     *
     * @param inputPath  输入目录路径
     * @param outputPath 输出目录路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

    /**
     * 递归拷贝文件（通过递归，将输入目录完整的拷贝到输出目录下）
     *
     * @param inputPath  输入目录路径
     * @param outputPath 输出目录路径
     */
    public static void copyFilesByRecursive(String inputPath, String outputPath) {
        // 创建输入输出文件
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        // 开始递归拷贝文件
        try {
            StaticGenerator.copyFileByRecursive(inputFile, outputFile);
        } catch (Exception e) {
            System.err.println("文件复制失败");
            e.printStackTrace();
        }
    }

    /**
     * 文件 A => 目录 B，则文件 A 放在目录 B 下
     * 文件 A => 文件 B，则文件 A 覆盖文件 B
     * 目录 A => 目录 B，则目录 A 放在目录 B 下
     * <p>
     * 核心思路：先创建目录，然后遍历目录内的文件，依次复制
     *
     * @param inputFile  输入文件
     * @param outputFile 输出文件
     * @throws IOException
     */
    private static void copyFileByRecursive(File inputFile, File outputFile) throws IOException {
        // 区分是文件还是目录
        if (inputFile.isDirectory()) {
            System.out.println("目录：" + inputFile.getName());
            File destOutputFile = new File(outputFile, inputFile.getName());
            // 如果是目录，首先创建目标目录
            if (!destOutputFile.exists()) {
                destOutputFile.mkdirs();
            }
            // 获取目录下的所有文件和子目录
            File[] files = inputFile.listFiles();
            // 无子文件，直接结束
            if (ArrayUtil.isEmpty(files)) {
                return;
            }
            for (File file : files) {
                // 递归拷贝下一层文件
                StaticGenerator.copyFileByRecursive(file, destOutputFile);
            }
        } else {
            // 是文件，直接复制到目标目录下
            Path destPath = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
