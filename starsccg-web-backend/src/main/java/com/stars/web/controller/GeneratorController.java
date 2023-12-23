package com.stars.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.stars.web.annotation.AuthCheck;
import com.stars.web.common.BaseResponse;
import com.stars.web.common.DeleteRequest;
import com.stars.web.common.ErrorCode;
import com.stars.web.common.ResultUtils;
import com.stars.web.constant.UserConstant;
import com.stars.web.exception.BusinessException;
import com.stars.web.exception.ThrowUtils;
import com.stars.web.manager.CosManager;
import com.stars.web.meta.Meta;
import com.stars.web.model.dto.generator.GeneratorAddRequest;
import com.stars.web.model.dto.generator.GeneratorEditRequest;
import com.stars.web.model.dto.generator.GeneratorQueryRequest;
import com.stars.web.model.dto.generator.GeneratorUpdateRequest;
import com.stars.web.model.entity.Generator;
import com.stars.web.model.entity.User;
import com.stars.web.model.vo.GeneratorVo;
import com.stars.web.service.GeneratorService;
import com.stars.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成器控制器
 *
 * @author stars
 * @version 1.0.0
 */
@RestController
@RequestMapping("/generator")
@Slf4j
public class GeneratorController {

    @Resource
    private GeneratorService generatorService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 添加代码生成器
     *
     * @param generatorAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addGenerator(@RequestBody GeneratorAddRequest generatorAddRequest,
                                           HttpServletRequest request) {
        if (generatorAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorAddRequest, generator);

        List<String> generatorTagsList = generatorAddRequest.getGeneratorTags();
        String generatorTags = JSONUtil.toJsonStr(generatorTagsList);
        generator.setGeneratorTags(generatorTags);

        Meta.FileConfig fileConfig = generatorAddRequest.getGeneratorFileConfig();
        String generatorFileConfig = JSONUtil.toJsonStr(fileConfig);
        generator.setGeneratorFileConfig(generatorFileConfig);

        Meta.ModelConfig modelConfig = generatorAddRequest.getGeneratorModelConfig();
        String generatorModelConfig = JSONUtil.toJsonStr(modelConfig);
        generator.setGeneratorModelConfig(generatorModelConfig);

        // 校验代码生成器属性
        this.generatorService.validGenerator(generator, true, request);

        // 设置代码生成器的添加人
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        generator.setGeneratorUserId(loginUserId);

        // 校验代码生成器是否成功保存
        boolean result = this.generatorService.save(generator);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 返回添加的代码生成器 ID
        Long newGeneratorId = generator.getId();
        return ResultUtils.success(newGeneratorId);
    }

    /**
     * 删除代码生成器（本人或管理员）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteGenerator(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 根据 ID 判断待删除的代码生成器是否存在
        Long deleteGeneratorId = deleteRequest.getId();
        Generator deleteGenerator = this.generatorService.getById(deleteGeneratorId);
        ThrowUtils.throwIf(deleteGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 权限校验，仅本人或管理员可删除
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Long deleteGeneratorUserId = deleteGenerator.getGeneratorUserId();
        if (!deleteGeneratorUserId.equals(loginUserId) && !this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 返回待删除的代码生成器是否删除成功
        boolean flag = this.generatorService.removeById(deleteGeneratorId);
        return ResultUtils.success(flag);
    }

    /**
     * 更新代码生成器（管理员）
     *
     * @param generatorUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateGenerator(@RequestBody GeneratorUpdateRequest generatorUpdateRequest,
                                                 HttpServletRequest request) {
        if (generatorUpdateRequest == null || generatorUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 权限校验，仅管理员可更新
        if (!this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorUpdateRequest, generator);

        List<String> generatorTagsList = generatorUpdateRequest.getGeneratorTags();
        String generatorTags = JSONUtil.toJsonStr(generatorTagsList);
        generator.setGeneratorTags(generatorTags);

        Meta.FileConfig fileConfig = generatorUpdateRequest.getGeneratorFileConfig();
        String generatorFileConfig = JSONUtil.toJsonStr(fileConfig);
        generator.setGeneratorFileConfig(generatorFileConfig);

        Meta.ModelConfig modelConfig = generatorUpdateRequest.getGeneratorModelConfig();
        String generatorModelConfig = JSONUtil.toJsonStr(modelConfig);
        generator.setGeneratorModelConfig(generatorModelConfig);

        // 校验代码生成器属性
        this.generatorService.validGenerator(generator, false, request);

        // 根据 ID 判断代码生成器是否存在
        Long updateGeneratorId = generatorUpdateRequest.getId();
        Generator oldGenerator = generatorService.getById(updateGeneratorId);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 返回是否成功更新代码生成器
        boolean result = generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据 ID 获取代码生成器视图
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<GeneratorVo> getGeneratorVoById(Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 根据 ID 获取未脱敏的代码生成器
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 获取代码生成器视图
        GeneratorVo generatorVo = this.generatorService.getGeneratorVo(generator, request);
        return ResultUtils.success(generatorVo);
    }

    /**
     * 分页获取代码生成器列表（仅管理员）
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Generator>> listGeneratorByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                             HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 权限校验，仅管理员可更新
        if (!this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 获取代码生成器分页
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest, request));
        return ResultUtils.success(generatorPage);
    }

    /**
     * 分页获取代码生成器视图列表
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<GeneratorVo>> listGeneratorVoByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                 HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 获取代码生成器分页
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest, request));

        // 获取代码生成器视图分页
        Page<GeneratorVo> generatorVoPage = this.generatorService.getGeneratorVoPage(generatorPage, request);
        return ResultUtils.success(generatorVoPage);
    }

    /**
     * 分页获取当前用户的代码生成器视图列表
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<GeneratorVo>> listMyGeneratorVoByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                   HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 为代码生成器查询请求设置当前登录用户 ID
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        generatorQueryRequest.setGeneratorUserId(loginUserId);

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 获取代码生成器分页
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest, request));

        // 获取代码生成器视图分页
        Page<GeneratorVo> generatorVoPage = this.generatorService.getGeneratorVoPage(generatorPage, request);
        return ResultUtils.success(generatorVoPage);
    }

    /**
     * 编辑代码生成器（本人或管理员）
     *
     * @param generatorEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editGenerator(@RequestBody GeneratorEditRequest generatorEditRequest,
                                               HttpServletRequest request) {
        if (generatorEditRequest == null || generatorEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorEditRequest, generator);

        List<String> generatorTagsList = generatorEditRequest.getGeneratorTags();
        String generatorTags = JSONUtil.toJsonStr(generatorTagsList);
        generator.setGeneratorTags(generatorTags);

        Meta.FileConfig fileConfig = generatorEditRequest.getGeneratorFileConfig();
        String generatorFileConfig = JSONUtil.toJsonStr(fileConfig);
        generator.setGeneratorFileConfig(generatorFileConfig);

        Meta.ModelConfig modelConfig = generatorEditRequest.getGeneratorModelConfig();
        String generatorModelConfig = JSONUtil.toJsonStr(modelConfig);
        generator.setGeneratorModelConfig(generatorModelConfig);

        // 参数校验
        this.generatorService.validGenerator(generator, false, request);

        // 根据 ID 判断待编辑的代码生成器是否存在
        Long editGeneratorId = generatorEditRequest.getId();
        Generator editGenerator = this.generatorService.getById(editGeneratorId);
        ThrowUtils.throwIf(editGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 权限校验，仅本人或管理员可编辑
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Long editGeneratorUserId = editGenerator.getGeneratorUserId();
        if (!editGeneratorUserId.equals(loginUserId) && !this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 返回是否成功编辑代码生成器
        boolean result = this.generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据 ID 下载代码生成器
     *
     * @param id
     * @return
     */
    @GetMapping("/download")
    public void downloadGeneratorById(long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        User loginUser = this.userService.getLoginUser(request);

        // 根据 ID 获取代码生成器
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 获取产物包路径
        String filepath = generator.getGeneratorDistPath();
        if (StrUtil.isBlank(filepath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产物包不存在");
        }

        // 追踪事件
        log.info("用户 {} 下载了 {}", loginUser, filepath);

        // COS 对象输入流
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }
}
