package com.stars.maker.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.stars.maker.generator.file.DynamicFileGenerator;
import com.stars.maker.meta.Meta;
import com.stars.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author stars
 * @version 1.0
 */
public class MainGenerator {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TemplateException {

        // 获取元信息
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);

        // 声明输出根路径
        // 模块单独打开时，为当前模块根路径
        String projectPath = System.getProperty("user.dir");
        // 输出的根路径
        String outputPath = projectPath + File.separator + "generated";
        // 目录为空则创建目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource("");
        // 获取 resources 目录的绝对路径
        String inputResourcePath = classPathResource.getAbsolutePath();

        // Java 包的基础路径
        // com.stars
        String outputBasePackage = meta.getBasePackage();
        // com/stars
        String outputBasePackagePath = StrUtil.join("\\", StrUtil.split(outputBasePackage, "."));
        // generated/src/main/java/com/stars
        String outputBaseJavaPackagePath = outputPath + File.separator + "src\\main\\java\\" + outputBasePackagePath;

        // 生成 model.DataModel.java 文件
        String inputFilePath = inputResourcePath + "templates/java/model/DataModel.java.ftl";
        String outputFilePath = outputBaseJavaPackagePath + File.separator + "model\\DataModel.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
    }
}
