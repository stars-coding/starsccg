package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

<#macro generateFile indent fileInfo>
${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
    <#if fileInfo.generateType == "static">
${indent}// 生成静态文件
${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
    <#else>
${indent}// 生成动态文件
${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
    </#if>
</#macro>
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
        // 读取元信息输入输出根路径
        String inputRootPath = "<#if fileConfig.inputRootPath??>${fileConfig.inputRootPath}</#if>";
        String outputRootPath = "<#if fileConfig.outputRootPath??>${fileConfig.outputRootPath}</#if>";

        // 声明输入输出路径
        String inputPath;
        String outputPath;
<#-- 获取模型变量 -->
<#list modelConfig.models as modelInfo>
    <#-- 有分组 -->
    <#if modelInfo.groupKey??>
        <#list modelInfo.models as subModelInfo>

        ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
        </#list>
    <#else>

        ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
    </#if>
</#list>
<#list fileConfig.files as fileInfo>
    <#if fileInfo.groupKey??>

        // groupKey = ${fileInfo.groupKey}
        <#if fileInfo.condition??>
        if (${fileInfo.condition}) {
            <#list fileInfo.files as fileInfo>
            <@generateFile indent="            " fileInfo=fileInfo/>
            </#list>
        }
        <#else>
            <#list fileInfo.files as fileInfo>

        <@generateFile indent="        " fileInfo=fileInfo/>
            </#list>
        </#if>
    <#else>
        <#if fileInfo.condition??>

        if (${fileInfo.condition}) {
            <@generateFile indent="            " fileInfo=fileInfo/>
        }
        <#else>

        <@generateFile indent="        " fileInfo=fileInfo/>
        </#if>
    </#if>
</#list>
    }
}
