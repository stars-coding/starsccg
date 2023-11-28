package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 *
<#if author??> * @author ${author}</#if>
<#if version??> * @version ${version}</#if>
 */
public class MainGenerator {

    /**
     * 生成文件
     *
     * @param model 数据模型对象
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(DataModel model) throws TemplateException, IOException {

     <#list modelConfig.models as modelInfo>
         ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
     </#list>

        // 读取元信息输入输出根路径
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        // 声明输入输出路径
        String inputPath;
        String outputPath;
    <#list fileConfig.files as fileInfo>

        <#if fileInfo.condition??>
        if (${fileInfo.condition}) {
            inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
            outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
            <#if fileInfo.generateType == "static">
             // 生成静态文件
             StaticGenerator.copyFilesByHutool(inputPath, outputPath);
            <#else>
             // 生成动态文件
             DynamicGenerator.doGenerate(inputPath, outputPath, model);
            </#if>
         }
        <#else>
        inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        <#if fileInfo.generateType == "static">
        // 生成静态文件
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
        <#else>
        // 生成动态文件
        DynamicGenerator.doGenerate(inputPath, outputPath, model);
        </#if>
        </#if>
</#list>
    }
}
