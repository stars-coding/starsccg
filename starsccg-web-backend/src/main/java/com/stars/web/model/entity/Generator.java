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
    private String tags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private String fileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private String modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 代码生成器状态
     */
    private Integer status;

    /**
     * 代码生成器添加人的用户主键
     */
    private Long userId;

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
