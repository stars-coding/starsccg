package com.stars.maker.meta.enums;

import lombok.Getter;

/**
 * 文件生成类型枚举
 *
 * @author stars
 * @version 1.0.0
 */
@Getter
public enum FileGenerateTypeEnum {

    DYNAMIC("动态", "dynamic"),
    STATIC("静态", "static");

    private final String text;

    private final String value;

    FileGenerateTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
