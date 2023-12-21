package com.stars.web.model.dto.generator;

import com.stars.web.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成器查询请求
 *
 * @author stars
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneratorQueryRequest extends PageRequest implements Serializable {

    /**
     * 代码生成器主键
     */
    private Long id;

    /**
     * 非代码生成器主键
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 代码生成器标签列表
     */
    private List<String> generatorTags;

    /**
     * 至少有一个代码生成器标签
     */
    private List<String> orGeneratorTags;

    /**
     * 代码生成器添加人的用户主键
     */
    private Long generatorUserId;

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
     * 代码生成器产物路径
     */
    private String generatorDistPath;

    /**
     * 代码生成器状态
     */
    private Integer generatorStatus;

    private static final long serialVersionUID = 1L;
}
