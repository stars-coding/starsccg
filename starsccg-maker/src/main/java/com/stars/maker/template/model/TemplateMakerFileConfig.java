package com.stars.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NoArgsConstructor
    @Data
    public static class FileInfoConfig {
        private String path;
        private List<FileFilterConfig> filterConfigList;
    }
}
