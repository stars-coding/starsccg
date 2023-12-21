package com.stars.web.model.dto.file;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新文件请求
 *
 * @author stars
 * @version 1.0.0
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

    private static final long serialVersionUID = 1L;
}
