package com.stars.maker.template.model;

import lombok.Data;

import java.util.List;

/**
 * 模板制作模型配置
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> models;
    private ModelGroupConfig modelGroupConfig;

    @Data
    public static class ModelInfoConfig {
        private String fieldName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;
        // 用于替换哪些文本
        private String replaceText;
    }

    @Data
    public static class ModelGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;
        private String type;
        private String description;
    }
}
