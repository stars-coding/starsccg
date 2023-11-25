package com.stars.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * 静态文件生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class StaticFileGenerator {

    /**
     * 胡图拷贝文件（通过 Hutool 工具类，将输入目录完整的拷贝到输出目录下）
     *
     * @param inputPath  输入目录路径
     * @param outputPath 输出目录路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
