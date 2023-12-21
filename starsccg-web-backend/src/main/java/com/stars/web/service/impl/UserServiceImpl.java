package com.stars.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stars.web.common.ErrorCode;
import com.stars.web.constant.CommonConstant;
import com.stars.web.constant.UserConstant;
import com.stars.web.exception.BusinessException;
import com.stars.web.mapper.UserMapper;
import com.stars.web.model.dto.user.UserQueryRequest;
import com.stars.web.model.entity.User;
import com.stars.web.model.enums.UserRoleEnum;
import com.stars.web.model.vo.LoginUserVo;
import com.stars.web.model.vo.UserVo;
import com.stars.web.service.UserService;
import com.stars.web.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author stars
 * @version 1.0.0
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "stars";

    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1、参数校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，用户账号为空或用户密码为空或校验密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，用户账号长度过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，用户密码长度过短");
        }
        // 用户密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，两次输入的密码不一致");
        }

        synchronized (userAccount.intern()) {
            // 账号不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，账号重复");
            }

            // 2、加密密码
            String encryptPassword = DigestUtils.md5DigestAsHex((UserServiceImpl.SALT + userPassword).getBytes());

            // 3. 用户入库
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }

            // 返回用户 ID
            Long id = user.getId();
            return id;
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、参数校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败，用户账号为空或用户密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败，用户账号长度过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败，用户密码长度过短");
        }

        // 2、加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((UserServiceImpl.SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            this.log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败，用户不存在或密码错误");
        }

        // 3、记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);

        // 返回当前登录用户视图
        LoginUserVo loginUserVo = this.getLoginUserVo(user);
        return loginUserVo;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 判断是否已登录
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        Long id = currentUser.getId();
        currentUser = this.getById(id);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 判断是否已登录
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }

        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long id = currentUser.getId();
        currentUser = this.getById(id);
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;

        // 判断是否为管理员
        boolean isAdmin = this.isAdmin(user);
        return isAdmin;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    @Override
    public boolean isAdmin(User user) {
        // 获取当前用户的权限
        String userRole = user.getUserRole();

        // 判断是否为管理员
        boolean isAdmin = user != null && UserRoleEnum.ADMIN.getValue().equals(userRole);
        return isAdmin;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "注销失败，当前并未登录");
        }

        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前登录用户视图
     *
     * @param user
     * @return
     */
    @Override
    public LoginUserVo getLoginUserVo(User user) {
        if (user == null) {
            return null;
        }

        // 获取当前登录用户视图
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        return loginUserVo;
    }

    /**
     * 获取用户视图
     *
     * @param user
     * @return
     */
    @Override
    public UserVo getUserVo(User user) {
        if (user == null) {
            return null;
        }

        // 获取用户视图
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    /**
     * 获取用户视图列表
     *
     * @param userList
     * @return
     */
    @Override
    public List<UserVo> getUserVo(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }

        // 获取用户视图列表
        List<UserVo> userVoList = userList.stream().map(this::getUserVo).collect(Collectors.toList());
        return userVoList;
    }

    /**
     * 获取查询包装器
     *
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询失败，用户查询请求参数为空");
        }

        // 获取用户查询请求属性
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        // 构建包装器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
