package com.stars.maker.util.os;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.stars.maker.util.os.enums.OsTypeEnum;

/**
 * 操作系统类型
 *
 * @author stars
 * @version 1.0.0
 */
public class OsUtil {

    public static final String OS_TYPE = OsUtil.getOsType();

    private static String getOsType() {
        // 获取系统名称
        String osType = SystemUtil.get(SystemUtil.OS_NAME);
        // 判断赋值
        if (StrUtil.containsIgnoreCase(osType, OsTypeEnum.WINDOWS.getValue())) {
            return OsTypeEnum.WINDOWS.getValue();
        }
        if (StrUtil.containsIgnoreCase(osType, OsTypeEnum.LINUX.getValue())) {
            return OsTypeEnum.LINUX.getValue();
        }
        if (StrUtil.containsIgnoreCase(osType, OsTypeEnum.MAC.getValue())) {
            return OsTypeEnum.MAC.getValue();
        }
        return OsTypeEnum.OTHER.getValue();
    }
}
