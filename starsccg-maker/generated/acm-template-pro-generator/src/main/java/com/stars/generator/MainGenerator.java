package com.stars.generator;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class MainGenerator {

    /**
     * 生成文件
     *
     * @param model 数据模型对象
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws TemplateException, IOException {

        // 读取元信息输入输出根路径
        String inputRootPath = ".source/acm-template-pro";
        String outputRootPath = "generated";

        // 声明输入输出路径
        String inputPath;
        String outputPath;

        inputPath = new File(inputRootPath, "src/com/stars/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/stars/acm/MainTemplate.java").getAbsolutePath();
        // 生成动态文件
        DynamicGenerator.doGenerate(inputPath, outputPath, model);

        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        // 生成静态文件
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
    }
}
