package com.stars.maker.model;

import lombok.Data;

/**
 * 动态模版配置
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class DataModel {

    /**
     * 是否生成循环
     */
    private boolean loop = true;

    /**
     * 作者注释
     */
    private String author = "默认作者";

    /**
     * 输出信息
     */
    private String outputText = "默认输出信息";
}
