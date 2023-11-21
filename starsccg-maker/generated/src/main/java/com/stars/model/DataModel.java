package com.stars.model;

import lombok.Data;

/**
 * 数据模型
 *
 * @author stars
 * @version 1.0
 */
@Data
public class DataModel {

    /**
     * 是否循环
     */
    private boolean loop = true;

    /**
     * 作者注释
     */
    private String author = "stars";

    /**
     * 输出信息
     */
    private String outputText = "sum = ";
}
