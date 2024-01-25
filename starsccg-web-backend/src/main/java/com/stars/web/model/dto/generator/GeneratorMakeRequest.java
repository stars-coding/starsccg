package com.stars.web.model.dto.generator;

import com.stars.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;

/**
 * 代码生成器制作请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class GeneratorMakeRequest implements Serializable {

    /**
     * 元信息
     *
     * condition: "test"
     * groupKey: "分组主键"
     * groupName: "分组名称"
     * type: "分组"
     */
    private Meta meta;

    /**
     * 模板压缩包文件路径
     */
    private String zipFilePath;

    private static final long serialVersionUID = 1L;
}
