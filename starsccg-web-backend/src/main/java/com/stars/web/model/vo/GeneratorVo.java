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
public class GeneratorVo implements Serializable {

    /**
     * 代码生成器添加人
     */
    private UserVo userVo;

    /**
     * 代码生成器主键
     */
    private Long id;

    /**
     * 代码生成器名称
     */
    private String generatorName;

    /**
     * 代码生成器描述
     */
    private String generatorDescription;

    /**
     * 代码生成器作者
     */
    private String generatorAuthor;

    /**
     * 代码生成器版本
     */
    private String generatorVersion;

    /**
     * 代码生成器基础包
     */
    private String generatorBasePackage;

    /**
     * 代码生成器图片
     */
    private String generatorPicture;

    /**
     * 代码生成器标签列表(JSON数组)
     */
    private List<String> generatorTags;

    /**
     * 代码生成器文件配置(JSON字符串)
     */
    private Meta.FileConfig generatorFileConfig;

    /**
     * 代码生成器模型配置(JSON字符串)
     */
    private Meta.ModelConfig generatorModelConfig;

    /**
     * 代码生成器产物路径
     */
    private String generatorDistPath;

    /**
     * 代码生成器状态
     */
    private Integer generatorStatus;

    /**
     * 代码生成器添加人的用户主键
     */
    private Long generatorUserId;

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
     * List<String> generatorTags --> String generatorTags
     * Meta.FileConfig generatorFileConfig --> String generatorFileConfig
     * Meta.ModelConfig generatorModelConfig --> String generatorModelConfig
     *
     * @param generatorVo
     * @return
     */
    public static Generator voToObj(GeneratorVo generatorVo) {
        if (generatorVo == null) {
            return null;
        }

        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorVo, generator);

        List<String> generatorTagsList = generatorVo.getGeneratorTags();
        String generatorTags = JSONUtil.toJsonStr(generatorTagsList);
        generator.setGeneratorTags(generatorTags);

        Meta.FileConfig fileConfig = generatorVo.getGeneratorFileConfig();
        String generatorFileConfig = JSONUtil.toJsonStr(fileConfig);
        generator.setGeneratorFileConfig(generatorFileConfig);

        Meta.ModelConfig modelConfig = generatorVo.getGeneratorModelConfig();
        String generatorModelConfig = JSONUtil.toJsonStr(modelConfig);
        generator.setGeneratorModelConfig(generatorModelConfig);

        return generator;
    }

    /**
     * 实体转换为视图
     * 对象转包装类
     * 目的：解决实体对象转化为视图对象时，出现属性类型不匹配问题
     * 具体：实体属性-->视图属性
     * String generatorTags --> List<String> generatorTags
     * String generatorFileConfig --> Meta.FileConfig generatorFileConfig
     * String generatorModelConfig --> Meta.ModelConfig generatorModelConfig
     *
     * @param generator
     * @return
     */
    public static GeneratorVo objToVo(Generator generator) {
        if (generator == null) {
            return null;
        }

        GeneratorVo generatorVo = new GeneratorVo();
        BeanUtils.copyProperties(generator, generatorVo);

        String generatorTags = generator.getGeneratorTags();
        List<String> generatorTagsList = JSONUtil.toList(generatorTags, String.class);
        generatorVo.setGeneratorTags(generatorTagsList);

        String fileConfig = generator.getGeneratorFileConfig();
        Meta.FileConfig generatorFileConfig = JSONUtil.toBean(fileConfig, Meta.FileConfig.class);
        generatorVo.setGeneratorFileConfig(generatorFileConfig);

        String modelConfig = generator.getGeneratorModelConfig();
        Meta.ModelConfig generatorModelConfig = JSONUtil.toBean(modelConfig, Meta.ModelConfig.class);
        generatorVo.setGeneratorModelConfig(generatorModelConfig);

        return generatorVo;
    }
}
