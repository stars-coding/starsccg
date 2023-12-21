package com.stars.web.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码(加密)
     */
    private String userPassword;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色(user/admin/ban)
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
