package com.stars.springbootinit.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    private static final long serialVersionUID = 3191241716373120793L;
}
