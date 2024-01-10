package com.stars.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;

/**
 * 代码生成器缓存请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class GeneratorCacheRequest implements Serializable {

    /**
     * 代码生成器主键
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
