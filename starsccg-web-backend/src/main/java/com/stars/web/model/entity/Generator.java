package com.stars.web.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代码生成器表
 *
 * @author stars
 * @version 1.0.0
 */
@TableName(value = "generator")
@Data
public class Generator implements Serializable {

    /**
     * 代码生成器主键
     */
    @TableId(type = IdType.AUTO)
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
    private String generatorTags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private String generatorFileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private String generatorModelConfig;

    /**
     * 代码生成器产物路径
     */
    private String generatorDistPath;

    /**
     * 代码生成器状态
     */
    private Integer generatorStatus;

    /**
     * 代码生成器添加人的用户主键
     */
    private Long generatorUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Byte isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
