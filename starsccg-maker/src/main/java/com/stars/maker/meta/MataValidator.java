package com.stars.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.stars.maker.meta.enums.FileGenerateTypeEnum;
import com.stars.maker.meta.enums.FileTypeEnum;
import com.stars.maker.meta.enums.ModelTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元信息校验
 *
 * @author stars
 * @version 1.0.0
 */
public class MataValidator {

    /**
     * 校验全部
     *
     * @param meta
     */
    public static void doValidAndFill(Meta meta) {
        // 校验三部分
        MataValidator.validAndFillMetaRoot(meta);
        MataValidator.validAndFillFileConfig(meta);
        MataValidator.validAndFillModelConfig(meta);
    }

    /**
     * 校验元信息基础部分
     *
     * @param meta
     */
    public static void validAndFillMetaRoot(Meta meta) {
        // 校验并填充默认值
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        meta.setName(name);
        String description = StrUtil.emptyToDefault(meta.getDescription(), "我的模板代码生成器");
        meta.setDescription(description);
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.stars");
        meta.setBasePackage(basePackage);
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0.0");
        meta.setVersion(version);
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "stars");
        meta.setAuthor(author);
        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setCreateTime(createTime);
    }

    /**
     * 校验文件配置
     *
     * @param meta
     */
    public static void validAndFillFileConfig(Meta meta) {
        // 校验 modelConfig ，为空直接返回
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        // 校验 sourceRootPath ，有问题抛出异常
        String sourceRootPath = fileConfig.getSourceRootPath();
        // isBlank 为 true 表示 对象 == null 或 去掉首尾空白字串为空串
        // isBlank 为 false 表示 对象 != null 且 去掉首尾空白字串不为空串
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }
        // 校验 inputRootPath ，有问题抛设置默认值
        // inputRootPath 为 .source + sourceRootPath 的最后一个层级路径
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath = ".source"
                + File.separator
                + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        // isEmpty 为 true 表示 对象 == null 或 对象 == 空串
        // isEmpty 为 false 表示 对象 != null 且 对象 != 空串
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }
        // 校验 outputRootPath ，有问题抛设置默认值
        // outputRootPath 默认为当前路径下的 generated
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath = "generated";
        // isEmpty 为 true 表示 对象 == null 或 对象 == 空串
        // isEmpty 为 false 表示 对象 != null 且 对象 != 空串
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }
        // 校验 fileConfigType ，有问题抛设置默认值
        String fileConfigType = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getValue();
        // isEmpty 为 true 表示 对象 == null 或 对象 == 空串
        // isEmpty 为 false 表示 对象 != null 且 对象 != 空串
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfig.setType(defaultType);
        }
        // 校验 fileInfoList ，为空直接返回
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        // isNotEmpty 为 true 表示 list != null 且 list 有元素
        // isNotEmpty 为 false 表示 list == null 或 list 无元素
        if (!CollectionUtil.isNotEmpty(fileInfoList)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
            // 类型为 group ，不进行校验
            String type = fileInfo.getType();
            if (FileTypeEnum.GROUP.getValue().equals(type)) {
                continue;
            }
            // 校验 inputPath ，有问题抛出异常
            String inputPath = fileInfo.getInputPath();
            // isBlank 为 true 表示 对象 == null 或 去掉首尾空白字串为空串
            // isBlank 为 false 表示 对象 != null 且 去掉首尾空白字串不为空串
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            // 校验 outputPath ，有问题抛设置默认值
            // todo 动态动态生成文件时，存在模板文件与原始文件在路径上存在的差异
            String outputPath = fileInfo.getOutputPath();
            // isEmpty 为 true 表示 对象 == null 或 对象 == 空串
            // isEmpty 为 false 表示 对象 != null 且 对象 != 空串
            if (StrUtil.isEmpty(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }
            // 校验 type ，有问题依据路径设置默认值
            // type：默认 inputPath 有文件后缀为 file，否则为 dir
            type = fileInfo.getType();
            // isBlank 为 true 表示 对象 == null 或 去掉首尾空白字串为空串
            // isBlank 为 false 表示 对象 != null 且 去掉首尾空白字串不为空串
            if (StrUtil.isBlank(type)) {
                // 有无文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }
            // 校验 generateType ，有问题依据路径设置默认值
            // generateType：如果文件结尾不为 *.ftl ，默认为 static，否则为 dynamic
            String generateType = fileInfo.getGenerateType();
            // isBlank 为 true 表示 对象 == null 或 去掉首尾空白字串为空串
            // isBlank 为 false 表示 对象 != null 且 去掉首尾空白字串不为空串
            if (StrUtil.isBlank(generateType)) {
                // 是否为动态模板
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    /**
     * 校验模型配置
     *
     * @param meta
     */
    public static void validAndFillModelConfig(Meta meta) {
        // 校验 modelConfig ，为空直接返回
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        // 校验 modelInfoList ，为空直接返回
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelConfig.getModels();
        // isNotEmpty 为 true 表示 list != null 且 list 有元素
        // isNotEmpty 为 false 表示 list == null 或 list 无元素
        if (!CollectionUtil.isNotEmpty(modelInfoList)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo modelInfo : modelInfoList) {
            // 类型为 group ，不进行校验
            String groupKey = modelInfo.getGroupKey();
            if (StrUtil.isNotEmpty(groupKey)) {
                // 生成中间参数
                List<Meta.ModelConfig.ModelInfo> subModelInfoList = modelInfo.getModels();
                String allArgsStr = modelInfo.getModels().stream()
                        .map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName()))
                        .collect(Collectors.joining(", "));
                modelInfo.setAllArgsStr(allArgsStr);
                continue;
            }
            // 校验 fieldName ，有问题抛出异常
            String fieldName = modelInfo.getFieldName();
            // isBlank 为 true 表示 对象 == null 或 去掉首尾空白字串为空串
            // isBlank 为 false 表示 对象 != null 且 去掉首尾空白字串不为空串
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }
            // 校验 modelInfoType ，有问题赋默认值
            // todo 如果碰到布尔类型怎么办？
            String modelInfoType = modelInfo.getType();
            // isEmpty 为 true 表示 对象 == null 或 对象 == 空串
            // isEmpty 为 false 表示 对象 != null 且 对象 != 空串
            if (StrUtil.isEmpty(modelInfoType)) {
                modelInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }
}
