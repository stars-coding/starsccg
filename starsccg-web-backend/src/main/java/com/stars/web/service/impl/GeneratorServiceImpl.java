package com.stars.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stars.web.common.ErrorCode;
import com.stars.web.constant.CommonConstant;
import com.stars.web.exception.BusinessException;
import com.stars.web.exception.ThrowUtils;
import com.stars.web.mapper.GeneratorMapper;
import com.stars.web.model.dto.generator.GeneratorQueryRequest;
import com.stars.web.model.entity.Generator;
import com.stars.web.model.entity.User;
import com.stars.web.model.vo.GeneratorVo;
import com.stars.web.model.vo.UserVo;
import com.stars.web.service.GeneratorService;
import com.stars.web.service.UserService;
import com.stars.web.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码生成器服务实现
 *
 * @author stars
 * @version 1.0.0
 */
@Service
@Slf4j
public class GeneratorServiceImpl extends ServiceImpl<GeneratorMapper, Generator> implements GeneratorService {

    @Resource
    private UserService userService;

    /**
     * 校验代码生成器
     *
     * @param generator
     * @param add
     * @param request
     */
    @Override
    public void validGenerator(Generator generator, boolean add, HttpServletRequest request) {
        if (generator == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验代码生成器属性
        String generatorName = generator.getGeneratorName();
        String generatorDescription = generator.getGeneratorDescription();
        String generatorAuthor = generator.getGeneratorAuthor();
        String generatorVersion = generator.getGeneratorVersion();
        String generatorBasePackage = generator.getGeneratorBasePackage();
        String generatorPicture = generator.getGeneratorPicture();
        String generatorTags = generator.getGeneratorTags();

        // 创建时，代码生成器属性不能为空
        if (add) {
            // 对于是 null 或 空串 或 空白串，则返回 true
            boolean condition = StringUtils.isAnyBlank(generatorName, generatorDescription, generatorAuthor,
                    generatorVersion, generatorBasePackage, generatorPicture, generatorTags);
            ThrowUtils.throwIf(condition, ErrorCode.PARAMS_ERROR);
        }

        // 有属性则校验
        // 数据库中的 generatorName 字段长度为 255 ，按照一个字符占 3 个字节算，255 / 3 = 85 ，取 80 ，保守
        if (StringUtils.isNotBlank(generatorName) && generatorName.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器名称为空或长度过长");
        }
        // 数据库中的 generatorDescription 字段类型为 text ，安全起见，取长度 300
        if (StringUtils.isNotBlank(generatorDescription) && generatorDescription.length() > 300) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器描述为空或长度过长");
        }
        // 数据库中的 generatorAuthor 字段长度为 255 ，按照一个字符占 3 个字节算，255 / 3 = 85 ，取 80 ，保守
        if (StringUtils.isNotBlank(generatorAuthor) && generatorAuthor.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器作者为空或长度过长");
        }
        // 数据库中的 generatorVersion 字段长度为 255 ，按照一个字符占 3 个字节算，255 / 3 = 85 ，取 80 ，保守
        if (StringUtils.isNotBlank(generatorVersion) && generatorVersion.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器版本号为空或长度过长");
        }
        // 数据库中的 generatorBasePackage 字段长度为 255 ，按照一个字符占 3 个字节算，255 / 3 = 85 ，取 80 ，保守
        if (StringUtils.isNotBlank(generatorBasePackage) && generatorBasePackage.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器基础包名为空或长度过长");
        }
        // 数据库中的 generatorPicture 字段长度为 255 ，按照一个字符占 3 个字节算，255 / 3 = 85 ，取 80 ，保守
        if (StringUtils.isNotBlank(generatorPicture) && generatorPicture.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器图片路径为空或长度过长");
        }
        // 数据库中的 generatorTags 字段长度为 1023 ，按照一个字符占 3 个字节算，1023 / 3 = 341 ，取 320 ，保守
        if (StringUtils.isNotBlank(generatorTags) && generatorTags.length() > 320) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器标签为空或长度过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @Override
    public QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest, HttpServletRequest request) {
        QueryWrapper<Generator> queryWrapper = new QueryWrapper<>();
        if (generatorQueryRequest == null) {
            return queryWrapper;
        }

        // 获取代码生成器查询请求的属性
        Long id = generatorQueryRequest.getId();
        Long notId = generatorQueryRequest.getNotId();
        String searchText = generatorQueryRequest.getSearchText();
        List<String> generatorTags = generatorQueryRequest.getGeneratorTags();
        Long generatorUserId = generatorQueryRequest.getGeneratorUserId();
        String generatorName = generatorQueryRequest.getGeneratorName();
        String generatorDescription = generatorQueryRequest.getGeneratorDescription();
        String generatorAuthor = generatorQueryRequest.getGeneratorAuthor();
        String generatorVersion = generatorQueryRequest.getGeneratorVersion();
        String generatorBasePackage = generatorQueryRequest.getGeneratorBasePackage();
        String generatorDistPath = generatorQueryRequest.getGeneratorDistPath();
        Integer generatorStatus = generatorQueryRequest.getGeneratorStatus();
        String sortField = generatorQueryRequest.getSortField();
        String sortOrder = generatorQueryRequest.getSortOrder();

        // 拼接查询条件
        // todo 待优化
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(
                    qw -> qw.like("generatorName", searchText)
                            .or().like("generatorDescription", searchText));
        }

        queryWrapper.like(StringUtils.isNotBlank(generatorName), "generatorName", generatorName);
        queryWrapper.like(StringUtils.isNotBlank(generatorDescription), "generatorDescription", generatorDescription);

        if (CollUtil.isNotEmpty(generatorTags)) {
            for (String generatorTag : generatorTags) {
                queryWrapper.like("generatorTags", "\"" + generatorTag + "\"");
            }
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "notId", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorUserId), "generatorUserId", generatorUserId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorAuthor), "generatorAuthor", generatorAuthor);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorVersion), "generatorVersion", generatorVersion);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorBasePackage), "generatorBasePackage", generatorBasePackage);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorDistPath), "generatorDistPath", generatorDistPath);
        queryWrapper.eq(ObjectUtils.isNotEmpty(generatorStatus), "generatorStatus", generatorStatus);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    /**
     * 获取代码生成器视图
     *
     * @param generator
     * @param request
     * @return
     */
    @Override
    public GeneratorVo getGeneratorVo(Generator generator, HttpServletRequest request) {
        GeneratorVo generatorVo = GeneratorVo.objToVo(generator);

        // 关联查询用户信息，为代码生成器视图中的用户视图属性赋值
        Long generatorUserId = generator.getGeneratorUserId();
        User user = null;
        if (generatorUserId != null && generatorUserId > 0) {
            user = this.userService.getById(generatorUserId);
        }
        UserVo userVo = this.userService.getUserVo(user);
        generatorVo.setUserVo(userVo);

        return generatorVo;
    }

    /**
     * 分页获取代码生成器视图
     *
     * @param generatorPage
     * @param request
     * @return
     */
    @Override
    public Page<GeneratorVo> getGeneratorVoPage(Page<Generator> generatorPage, HttpServletRequest request) {
        List<Generator> generatorList = generatorPage.getRecords();
        Page<GeneratorVo> generatorVoPage =
                new Page<>(generatorPage.getCurrent(), generatorPage.getSize(), generatorPage.getTotal());
        if (CollUtil.isEmpty(generatorList)) {
            return generatorVoPage;
        }

        // 关联查询用户信息，为代码生成器视图中的用户视图属性赋值
        Set<Long> generatorUserIdSet = generatorList.stream()
                .map(Generator::getGeneratorUserId)
                .collect(Collectors.toSet());
        Map<Long, List<User>> generatorUserIdUserListMap = this.userService.listByIds(generatorUserIdSet)
                .stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<GeneratorVo> generatorVoList = generatorList.stream()
                .map(generator -> {
                    GeneratorVo generatorVo = GeneratorVo.objToVo(generator);
                    Long generatorUserId = generator.getGeneratorUserId();
                    User user = null;
                    if (generatorUserIdUserListMap.containsKey(generatorUserId)) {
                        user = generatorUserIdUserListMap.get(generatorUserId).get(0);
                    }
                    generatorVo.setUserVo(this.userService.getUserVo(user));
                    return generatorVo;
                })
                .collect(Collectors.toList());
        generatorVoPage.setRecords(generatorVoList);

        return generatorVoPage;
    }
}
