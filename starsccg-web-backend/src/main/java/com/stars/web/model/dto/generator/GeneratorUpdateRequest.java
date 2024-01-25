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
    private String name;

    /**
     * 代码生成器描述
     */
    private String description;

    /**
     * 代码生成器作者
     */
    private String author;

    /**
     * 代码生成器版本
     */
    private String version;

    /**
     * 代码生成器基础包
     */
    private String basePackage;

    /**
     * 代码生成器图片
     */
    private String picture;

    /**
     * 代码生成器标签列表(JSON数组)
     */
    private List<String> tags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private Meta.FileConfig fileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private Meta.ModelConfig modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 代码生成器状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
