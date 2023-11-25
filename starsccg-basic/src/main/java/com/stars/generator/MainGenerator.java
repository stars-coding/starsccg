package com.stars.generator;

import com.stars.model.MainTemplateConfig;
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
     * 入口函数
     *
     * @param args
     * @throws TemplateException
     * @throws IOException
     */
    public static void main(String[] args) throws TemplateException, IOException {
        // 创建数据模型对象
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("stars");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("求和结果：");
        // 生成文件
        MainGenerator.doGenerate(mainTemplateConfig);
    }

    /**
     * 生成文件
     *
     * @param model 数据模型对象
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws IOException, TemplateException {
        // 模块单独打开时，为当前模块根路径
        String projectPath = System.getProperty("user.dir");
        // 顶级项目的根路径
        File parentFile = new File(projectPath).getParentFile();

        // 生成静态文件
        String inputStaticFilePath = new File(parentFile, "starsccg-demo-projects/acm-template").getAbsolutePath();
        String outputStaticFilePath = projectPath;
        StaticGenerator.copyFilesByRecursive(inputStaticFilePath, outputStaticFilePath);

        // 生成动态文件
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputStaticFilePath + File.separator
                + "acm-template/src/com/stars/acm/MainTemplate.java";
        DynamicGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }
}
