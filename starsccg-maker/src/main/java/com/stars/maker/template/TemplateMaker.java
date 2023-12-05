package com.stars.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.stars.maker.meta.Meta;
import com.stars.maker.meta.enums.FileGenerateTypeEnum;
import com.stars.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板制作工具
 *
 * @author stars
 * @version 1.0.0
 */
public class TemplateMaker {

    /**
     * 函数入口
     *
     * @param args
     */
    public static void main(String[] args) {

        // 一、输入信息
        // 1. 输入项目基本信息
        String name = "acm-template-generator";
        String description = "ACM 代码生成器";
        // 2. 输入文件信息
        String projectPath = System.getProperty("user.dir");
        String sourceRootPath = new File(projectPath).getParent() + File.separator + "starsccg-demo-projects/acm-template";
        // Win 系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        String fileInputPath = "src/com/stars/acm/MainTemplate.java";
        String fileOutputPath = fileInputPath + ".ftl";
        // 3. 输入模型参数信息
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("计算之和：");

        // 二、使用字符串替换，生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, "Sum: ", replacement);
        // 输出模板文件
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        // 1. 构造配置参数
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);
        Meta.FileConfig fileConfig = new Meta.FileConfig();
        meta.setFileConfig(fileConfig);
        fileConfig.setSourceRootPath(sourceRootPath);
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        fileConfig.setFiles(fileInfoList);
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        fileInfoList.add(fileInfo);
        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
        meta.setModelConfig(modelConfig);
        List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        modelConfig.setModels(modelInfoList);
        modelInfoList.add(modelInfo);
        // 2. 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
    }
}
