package com.stars.web.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author stars
 * @version 1.0.0
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    /**
     * 用户主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
