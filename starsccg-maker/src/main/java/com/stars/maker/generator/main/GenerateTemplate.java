package com.stars.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.stars.maker.generator.JarGenerator;
import com.stars.maker.generator.ScriptGenerator;
import com.stars.maker.generator.file.DynamicFileGenerator;
import com.stars.maker.meta.Meta;
import com.stars.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * 生成面板
 * 主流程
 * 主流程固化，分支流程扩展
 *
 * @author stars
 * @version 1.0.0
 */
public abstract class GenerateTemplate {

    /**
     * 生成
     *
     * @throws IOException
     * @throws TemplateException
     * @throws InterruptedException
     */
    public void doGenerate() throws IOException, TemplateException, InterruptedException {
        // 获取元信息
        Meta meta = MetaManager.getMetaObject();
        // 声明输出根路径
        String projectPath = System.getProperty("user.dir");
        // 输出的根路径，指在哪里生成这个代码生成器项目文件
        String outputPath = projectPath + "/" + "generated" + "/" + meta.getName();
        // 调用重载方法
        this.doGenerate(meta, projectPath);
    }

    /**
     * 生成
     *
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     * @throws InterruptedException
     */
    public void doGenerate(Meta meta, String outputPath) throws IOException, TemplateException, InterruptedException {
        // 输出路径格式标准化
        outputPath = outputPath.replaceAll("\\\\", "/");

        // 目录为空则创建目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 1、复制源文件，复制最终想要生成的代码及模板至代码生成器项目目录下
        String sourceCopyDestPath = this.copySource(meta, outputPath);

        // 2、生成代码，生成代码生成器的项目代码
        this.generateCode(meta, outputPath);

        // 3、构建 jar 包，将生成的代码生成器项目代码构建为 jar 包
        String jarPath = this.buildJar(meta, outputPath);

        // 4、封装脚本
        String shellOutputFilePath = this.buildScript(outputPath, jarPath);

        // 5、生成精简版的程序（产物包）
        this.buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
    }

    /**
     * 拷贝源文件
     *
     * @param meta
     * @param outputPath
     * @return
     */
    protected String copySource(Meta meta, String outputPath) {
        // 源代码及模板在代码生成器制作工具项目下的路径
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        // 将源代码及模板拷贝到代码生成器项目下的路径
        String sourceCopyDestPath = outputPath + "/" + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    /**
     * 生成代码
     *
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        // 读取 resources 目录
        String inputResourcePath = "";

        // Java 包的基础路径
        // com.stars
        String outputBasePackage = meta.getBasePackage();
        // com/stars
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        // generated/src/main/java/com/stars
        String outputBaseJavaPackagePath = outputPath + "/" + "src/main/java/" + outputBasePackagePath;

        // 声明输入输出路径
        String inputFilePath;
        String outputFilePath;

        // 生成 model.DataModel.java 文件
        inputFilePath = inputResourcePath + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.ConfigCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.GenerateCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.JsonGenerateCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/JsonGenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "cli/command/JsonGenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.ListCommand.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 cli.command.CommandExecutor.java 文件
        inputFilePath = inputResourcePath + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 Main.java 文件
        inputFilePath = inputResourcePath + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/" + "Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.DynamicGenerator.java 文件
        inputFilePath = inputResourcePath + "/" + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.MainGenerator.java 文件
        inputFilePath = inputResourcePath + "/" + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 generator.StaticGenerator.java 文件
        inputFilePath = inputResourcePath + "/" + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 pom.xml.ftl 文件
        inputFilePath = inputResourcePath + "templates/java/pom.xml.ftl";
        outputFilePath = outputPath + "/" + "pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // 生成 README.md.ftl 文件
        inputFilePath = inputResourcePath + "templates/java/README.md.ftl";
        outputFilePath = outputPath + "/" + "README.md";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
    }

    /**
     * 构建 jar 包
     *
     * @param meta
     * @param outputPath
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected String buildJar(Meta meta, String outputPath) throws IOException, InterruptedException {
        // 生成 jar 包
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
     * 构建脚本
     *
     * @param outputPath
     * @param jarPath
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected String buildScript(String outputPath, String jarPath) throws IOException, InterruptedException {
        // 封装脚本
        String shellOutputFilePath = outputPath + "/" + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    /**
     * 构建产物
     *
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     */
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        // 生成精简版的程序（产物包）
        String distOutputPath = outputPath + "-dist";
        // 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + "/" + "target";
        // 创建目录，会删除原有目录
        FileUtil.mkdir(targetAbsolutePath);
        // 拷贝 jar 包
        String jarAbsolutePath = outputPath + "/" + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        FileUtil.copy(shellOutputFilePath + ".bat", distOutputPath, true);
        // 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
        return distOutputPath;
    }

    /**
     * 制作压缩包
     *
     * @param outputPath
     * @return 压缩包路径
     */
    protected String buildZip(String outputPath) {
        String zipPath = outputPath + ".zip";
        ZipUtil.zip(outputPath, zipPath);
        return zipPath;
    }
}
