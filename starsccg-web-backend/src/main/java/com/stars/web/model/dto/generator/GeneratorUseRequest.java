package com.stars.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 代码生成器使用请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class GeneratorUseRequest implements Serializable {

    /**
     * 代码生成器主键
     */
    private Long id;

    /**
     * 数据模型
     */
    private Map<String, Object> dataModel;

    private static final long serialVersionUID = 1L;
}
