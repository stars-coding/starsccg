package com.stars.web.model.dto.generator;

import com.stars.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成器更新请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class GeneratorUpdateRequest implements Serializable {

    /**
     * 代码生成器主键
     */
    private Long id;

    /**
     * 代码生成器名称
     */
    private String generatorName;

    /**
     * 代码生成器描述
     */
    private String generatorDescription;

    /**
     * 代码生成器作者
     */
    private String generatorAuthor;

    /**
     * 代码生成器版本
     */
    private String generatorVersion;

    /**
     * 代码生成器基础包
     */
    private String generatorBasePackage;

    /**
     * 代码生成器图片
     */
    private String generatorPicture;

    /**
     * 代码生成器标签列表(JSON数组)
     */
    private List<String> generatorTags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private Meta.FileConfig generatorFileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private Meta.ModelConfig generatorModelConfig;

    /**
     * 代码生成器产物路径
     */
    private String generatorDistPath;

    /**
     * 代码生成器状态
     */
    private Integer generatorStatus;

    private static final long serialVersionUID = 1L;
}
