package com.stars.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stars.web.model.entity.Generator;

import java.util.Date;
import java.util.List;

/**
 * 代码生成器数据库操作
 *
 * @author stars
 * @version 1.0.0
 */
public interface GeneratorMapper extends BaseMapper<Generator> {

    /**
     * 查询代码生成器列表（包括已被删除的数据）
     *
     * @param minUpdateTime
     * @return
     */
    List<Generator> listGeneratorWithDelete(Date minUpdateTime);
}
