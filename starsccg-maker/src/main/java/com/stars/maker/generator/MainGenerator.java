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
 * 核心生成器
 *
 * @author stars
 * @version 1.0
 */
public class MainGenerator {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TemplateException, InterruptedException {

        // 获取元信息
        Meta meta = MetaManager.getMetaObject();

        // 声明输出根路径
        // 模块单独打开时，为当前模块根路径
        String projectPath = System.getProperty("user.dir");
        // 输出的根路径
//        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        String outputPath = meta.getFileConfig().getOutputRootPath() + File.separator + meta.getName();
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

        // 声明输入输出路径
        String inputFilePath;
        String outputFilePath;

        // 生成 model.DataModel.java 文件
        inputFilePath = inputResourcePath + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "model/DataModel.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.ConfigCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ConfigCommand.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.GenerateCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/GenerateCommand.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.ListCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ListCommand.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.CommandExecutor.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/CommandExecutor.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 Main.java 文件
        inputFilePath = inputResourcePath + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.DynamicGenerator.java 文件
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.MainGenerator.java 文件
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.StaticGenerator.java 文件
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 pom.xml.ftl 文件
        inputFilePath = inputResourcePath + "templates/java/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        outputFilePath = StrUtil.join("/", StrUtil.split(outputFilePath, "\\"));
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 构建 jar 包
        // generated/src/main/java/com/stars
        JarGenerator.doGenerate(outputPath);

        // 封装脚本
        String shellOutputFilePath = outputPath + File.separator + "generator";
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
    }
}
