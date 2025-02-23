package com.stars.maker.meta.enums;

import lombok.Getter;

/**
 * 模型类型枚举
 *
 * @author stars
 * @version 1.0.0
 */
@Getter
public enum ModelTypeEnum {

    STRING("字符串", "String"),
    BOOLEAN("布尔", "boolean");

    private final String text;

    private final String value;

    ModelTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
