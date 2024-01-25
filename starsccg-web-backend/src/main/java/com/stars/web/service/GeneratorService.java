package com.stars.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stars.web.model.dto.generator.GeneratorQueryRequest;
import com.stars.web.model.entity.Generator;
import com.stars.web.model.vo.GeneratorVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 代码生成器服务
 *
 * @author stars
 * @version 1.0.0
 */
public interface GeneratorService extends IService<Generator> {

    /**
     * 校验代码生成器
     *
     * @param generator
     * @param add
     */
    void validGenerator(Generator generator, boolean add);

    /**
     * 获取查询包装类
     *
     * @param generatorQueryRequest
     * @return
     */
    QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest);

    /**
     * 获取代码生成器视图
     *
     * @param generator
     * @param request
     * @return
     */
    GeneratorVO getGeneratorVO(Generator generator, HttpServletRequest request);

    /**
     * 分页获取代码生成器视图
     *
     * @param generatorPage
     * @param request
     * @return
     */
    Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage, HttpServletRequest request);
}
