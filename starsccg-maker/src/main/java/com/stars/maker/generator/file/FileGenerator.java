package com.stars.maker.generator.file;

import com.stars.maker.meta.Meta;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 文件生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class FileGenerator {

    /**
     * 生成文件
     *
     * @param model 数据模型对象
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Meta model) throws IOException, TemplateException {
        // 模块单独打开时，为当前模块根路径
        String projectPath = System.getProperty("user.dir");
        // 顶级项目的根路径
        File parentFile = new File(projectPath).getParentFile();

        // 生成静态文件
        String inputStaticFilePath = new File(parentFile, "starsccg-demo-projects/acm-template").getAbsolutePath();
        String outputStaticFilePath = projectPath;
        StaticFileGenerator.copyFilesByHutool(inputStaticFilePath, outputStaticFilePath);

        // 生成动态文件
        String inputDynamicFilePath = projectPath + "/" + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputStaticFilePath + "/" + "acm-template/src/com/stars/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }
}
