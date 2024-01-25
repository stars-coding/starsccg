package com.stars.web.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class UserVO implements Serializable {

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

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
