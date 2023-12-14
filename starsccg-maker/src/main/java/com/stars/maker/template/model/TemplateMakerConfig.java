package com.stars.maker.template.model;

import com.stars.maker.meta.Meta;
import lombok.Data;

/**
 * 模板制作配置
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class TemplateMakerConfig {

    private Long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();
    TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
}
