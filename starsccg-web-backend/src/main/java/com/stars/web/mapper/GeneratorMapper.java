package com.stars.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stars.web.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 代码生成器数据库操作
 *
 * @author stars
 * @version 1.0.0
 */
public interface GeneratorMapper extends BaseMapper<Generator> {

    @Select("SELECT id, generatorDistPath FROM generator WHERE isDelete = 1")
    List<Generator> listDeletedGenerator();
}
