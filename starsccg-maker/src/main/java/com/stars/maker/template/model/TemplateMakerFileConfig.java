package com.stars.maker.template.model;

import lombok.Data;

import java.util.List;

/**
 * 面板制作文件配置
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;
    private FileGroupConfig fileGroupConfig;

    @Data
    public static class FileInfoConfig {
        private String path;
        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    public static class FileGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;
    }
}
