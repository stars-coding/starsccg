package com.stars.maker.template.model;

import lombok.Data;

/**
 * 模板制作输出规则
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class TemplateMakerOutputConfig {

    // 从未分组文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}
