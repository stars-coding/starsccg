package com.stars.web.model.vo;

import cn.hutool.json.JSONUtil;
import com.stars.maker.meta.Meta;
import com.stars.web.model.entity.Generator;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 生成器视图（脱敏）
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class GeneratorVO implements Serializable {

    /**
     * 代码生成器添加人
     */
    private UserVO user;

    /**
     * 代码生成器主键
     */
    private Long id;

    /**
     * 代码生成器名称
     */
    private String name;

    /**
     * 代码生成器描述
     */
    private String description;

    /**
     * 代码生成器作者
     */
    private String author;

    /**
     * 代码生成器版本
     */
    private String version;

    /**
     * 代码生成器基础包
     */
    private String basePackage;

    /**
     * 代码生成器图片
     */
    private String picture;

    /**
     * 代码生成器标签列表(JSON数组)
     */
    private List<String> tags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private Meta.FileConfig fileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private Meta.ModelConfig modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 代码生成器状态
     */
    private Integer status;

    /**
     * 代码生成器添加人的用户主键
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 视图转换为实体
     * 包装类转对象
     * 目的：解决视图对象转化为实体对象时，出现属性类型不匹配问题
     * 具体：视图属性-->实体属性
     * List<String> tags --> String tags
     * Meta.FileConfig fileConfig --> String fileConfig
     * Meta.ModelConfig modelConfig --> String modelConfig
     *
     * @param generatorVO
     * @return
     */
    public static Generator voToObj(GeneratorVO generatorVO) {
        if (generatorVO == null) {
            return null;
        }

        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorVO, generator);

        List<String> tagList = generatorVO.getTags();
        generator.setTags(JSONUtil.toJsonStr(tagList));

        Meta.FileConfig fileConfig = generatorVO.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));

        Meta.ModelConfig modelConfig = generatorVO.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        return generator;
    }

    /**
     * 实体转换为视图
     * 对象转包装类
     * 目的：解决实体对象转化为视图对象时，出现属性类型不匹配问题
     * 具体：实体属性-->视图属性
     * String tags --> List<String> tags
     * String fileConfig --> Meta.FileConfig fileConfig
     * String modelConfig --> Meta.ModelConfig modelConfig
     *
     * @param generator
     * @return
     */
    public static GeneratorVO objToVo(Generator generator) {
        if (generator == null) {
            return null;
        }

        GeneratorVO generatorVO = new GeneratorVO();
        BeanUtils.copyProperties(generator, generatorVO);

        generatorVO.setTags(JSONUtil.toList(generator.getTags(), String.class));

        generatorVO.setFileConfig(JSONUtil.toBean(generator.getFileConfig(), Meta.FileConfig.class));

        generatorVO.setModelConfig(JSONUtil.toBean(generator.getModelConfig(), Meta.ModelConfig.class));

        return generatorVO;
    }
}
