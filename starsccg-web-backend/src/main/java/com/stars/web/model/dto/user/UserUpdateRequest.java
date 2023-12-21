package com.stars.web.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * 用户主键
     */
    private Long id;

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
