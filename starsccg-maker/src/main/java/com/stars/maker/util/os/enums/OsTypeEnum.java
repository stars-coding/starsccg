package com.stars.maker.util.os.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * 操作系统类型枚举
 *
 * @author stars
 * @version 1.0.0
 */
@Getter
public enum OsTypeEnum {

    WINDOWS("windows", "windows"),
    LINUX("linux", "linux"),
    MAC("mac", "mac"),
    OTHER("other", "other");

    private final String text;
    private final String value;

    OsTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static OsTypeEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (OsTypeEnum anEnum : OsTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
