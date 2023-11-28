package com.stars.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import com.stars.maker.meta.Meta;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * 动态文件生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class DynamicFileGenerator {

    /**
     * 生成动态文件
     *
     * @param inputPath  模板文件输入路径
     * @param outputPath 代码生成输出路径
     * @param model      数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Meta model) throws IOException, TemplateException {
        // 创建 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件所在的路径
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("UTF-8");
        configuration.setEncoding(Locale.getDefault(), "UTF-8");

        // 创建模板对象，加载指定模板
        String templateName = new File(inputPath).getName();
//        Template template = configuration.getTemplate(templateName);
        Template template = configuration.getTemplate(templateName, "UTF-8");

        // 创建数据模型
//        DataModel mainTemplateConfig = new DataModel();
//        mainTemplateConfig.setAuthor("stars");
//        mainTemplateConfig.setLoop(false);
//        mainTemplateConfig.setOutputText("求和结果：");

        // 如果文件不存在，则新建文件
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        // 生成文件，指定最终的文件名称
//        Writer out = new FileWriter(outputPath);
        BufferedWriter out =
                new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(outputPath)), StandardCharsets.UTF_8));
        template.process(model, out);

        // 关闭输出流资源
        out.close();
    }
}
