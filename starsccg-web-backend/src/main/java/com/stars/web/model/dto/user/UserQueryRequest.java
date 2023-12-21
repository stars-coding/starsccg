package com.stars.web.model.dto.user;

import com.stars.web.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author stars
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户主键
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

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
