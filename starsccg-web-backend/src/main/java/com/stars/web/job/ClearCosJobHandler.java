package com.stars.web.job;

import cn.hutool.core.util.StrUtil;
import com.stars.web.manager.CosManager;
import com.stars.web.mapper.GeneratorMapper;
import com.stars.web.model.entity.Generator;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 清理 COS 工作调度器
 *
 * @author stars
 * @version 1.0.0
 */
@Component
@Slf4j
public class ClearCosJobHandler {

    @Resource
    private CosManager cosManager;

    @Resource
    private GeneratorMapper generatorMapper;

    /**
     * 清理 COS 工作调度器
     * 每天执行
     *
     * @throws Exception
     */
    @XxlJob("clearCosJobHandler")
    public void clearCosJobHandler() throws Exception {
        this.log.info("clearCosJobHandler start");
        // 编写业务逻辑
        // 1. 用户上传的模板制作文件（generator_make_template）
        this.cosManager.deleteDir("/generator_make_template/");
        // 2. 已删除的代码生成器对应的产物包文件（generator_dist）
        List<Generator> generatorList = this.generatorMapper.listDeletedGenerator();
        List<String> keyList = generatorList.stream().map(Generator::getGeneratorDistPath)
                .filter(StrUtil::isNotBlank)
                // 移除 '/' 前缀
                .map(generatorDistPath -> generatorDistPath.substring(1))
                .collect(Collectors.toList());
        this.cosManager.deleteObjects(keyList);
        this.log.info("clearCosJobHandler end");
    }
}
