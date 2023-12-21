package com.stars.web.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
