package com.stars.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.stars.maker.template.enums.FileFilterRangeEnum;
import com.stars.maker.template.enums.FileFilterRuleEnum;
import com.stars.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件过滤器
 *
 * @author stars
 * @version 1.0.0
 */
public class FileFilter {

    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigList
     * @param file
     * @return
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        // 所有过滤器校验结束的结果
        boolean result = true;
        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }
        // 循环过滤
        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            // 处理过滤范围
            String range = fileFilterConfig.getRange();
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }
            // 要过滤的原内容
            String content = null;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = file.getName();
                    break;
                case FILE_CONTENT:
                    content = FileUtil.readUtf8String(file);
                    break;
                default:
                    break;
            }
            // 处理过滤规则和过滤值
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
            FileFilterRuleEnum filterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (filterRuleEnum == null) {
                continue;
            }
            switch (filterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
                    break;
            }
            // 有一个不满足，直接返回
            if (!result) {
                return false;
            }
        }
        // 都满足
        return true;
    }

    /**
     * 过滤文件
     * 对某个文件或目录进行过滤，返回文件列表
     *
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        // 根据路径获取所有文件
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> FileFilter.doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }
}
