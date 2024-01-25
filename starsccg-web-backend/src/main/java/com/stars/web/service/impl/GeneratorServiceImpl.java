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
import com.stars.web.model.vo.GeneratorVO;
import com.stars.web.model.vo.UserVO;
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
     */
    @Override
    public void validGenerator(Generator generator, boolean add) {
        if (generator == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验代码生成器属性
        String name = generator.getName();
        String description = generator.getDescription();
        String author = generator.getAuthor();
        String version = generator.getVersion();
        String basePackage = generator.getBasePackage();
        String picture = generator.getPicture();
        String tags = generator.getTags();

        // 创建时，代码生成器属性不能为空
        if (add) {
            // 对于是 null 或 空串 或 空白串，则返回 true
            boolean condition = StringUtils.isAnyBlank(name, description, author, version, basePackage, picture, tags);
            ThrowUtils.throwIf(condition, ErrorCode.PARAMS_ERROR);
        }

        // 有属性则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器名称为空或长度过长");
        }
        // 数据库中的 description 字段类型为 text ，安全起见，取长度 300
        if (StringUtils.isNotBlank(description) && description.length() > 300) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器描述为空或长度过长");
        }
        if (StringUtils.isNotBlank(author) && author.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器作者为空或长度过长");
        }
        if (StringUtils.isNotBlank(version) && version.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器版本号为空或长度过长");
        }
        if (StringUtils.isNotBlank(basePackage) && basePackage.length() > 255) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器基础包名为空或长度过长");
        }
        if (StringUtils.isNotBlank(picture) && picture.length() > 255) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器图片路径为空或长度过长");
        }
        if (StringUtils.isNotBlank(tags) && tags.length() > 1023) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验失败，代码生成器标签为空或长度过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param generatorQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest) {
        QueryWrapper<Generator> queryWrapper = new QueryWrapper<>();
        if (generatorQueryRequest == null) {
            return queryWrapper;
        }

        // 获取代码生成器查询请求的属性
        Long id = generatorQueryRequest.getId();
        Long notId = generatorQueryRequest.getNotId();
        String searchText = generatorQueryRequest.getSearchText();
        List<String> tags = generatorQueryRequest.getTags();
        Long userId = generatorQueryRequest.getUserId();
        String name = generatorQueryRequest.getName();
        String description = generatorQueryRequest.getDescription();
        String author = generatorQueryRequest.getAuthor();
        String version = generatorQueryRequest.getVersion();
        String basePackage = generatorQueryRequest.getBasePackage();
        String distPath = generatorQueryRequest.getDistPath();
        Integer status = generatorQueryRequest.getStatus();
        String sortField = generatorQueryRequest.getSortField();
        String sortOrder = generatorQueryRequest.getSortOrder();

        // 拼接查询条件
        // todo 待优化
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
        }

        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
//
//        if (CollUtil.isNotEmpty(tags)) {
//            for (String tag : tags) {
//                queryWrapper.like("tags", "\"" + tag + "\"");
//            }
//        }

        // ChatGPT 优化，将标签查询拼接由 AND 改为 OR
        if (CollUtil.isNotEmpty(tags)) {
            queryWrapper.and(wrapper -> {
                for (String tag : tags) {
                    System.out.println("----------------------" + tag);
                    wrapper.or().like("tags", "%" + tag + "%");
                }
            });
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "notId", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(StringUtils.isNotBlank(author), "author", author);
        queryWrapper.eq(StringUtils.isNotBlank(version), "version", version);
        queryWrapper.eq(StringUtils.isNotBlank(basePackage), "basePackage", basePackage);
        queryWrapper.eq(StringUtils.isNotBlank(distPath), "distPath", distPath);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
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
    public GeneratorVO getGeneratorVO(Generator generator, HttpServletRequest request) {
        GeneratorVO generatorVO = GeneratorVO.objToVo(generator);

        // 关联查询用户信息，为代码生成器视图中的用户视图属性赋值
        Long userId = generator.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = this.userService.getById(userId);
        }
        UserVO userVO = this.userService.getUserVO(user);
        generatorVO.setUser(userVO);

        return generatorVO;
    }

    /**
     * 分页获取代码生成器视图
     *
     * @param generatorPage
     * @param request
     * @return
     */
    @Override
    public Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage, HttpServletRequest request) {
        List<Generator> generatorList = generatorPage.getRecords();
        Page<GeneratorVO> generatorVOPage = new Page<>(generatorPage.getCurrent(), generatorPage.getSize(), generatorPage.getTotal());
        if (CollUtil.isEmpty(generatorList)) {
            return generatorVOPage;
        }

        // 关联查询用户信息，为代码生成器视图中的用户视图属性赋值
        Set<Long> userIdSet = generatorList.stream().map(Generator::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = this.userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<GeneratorVO> generatorVOList = generatorList.stream().map(generator -> {
            GeneratorVO generatorVO = GeneratorVO.objToVo(generator);
            Long userId = generator.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            generatorVO.setUser(this.userService.getUserVO(user));
            return generatorVO;
        }).collect(Collectors.toList());
        generatorVOPage.setRecords(generatorVOList);

        return generatorVOPage;
    }
}
