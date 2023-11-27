package com.stars.springbootinit.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

    private static final long serialVersionUID = 3191241716373120793L;
}
