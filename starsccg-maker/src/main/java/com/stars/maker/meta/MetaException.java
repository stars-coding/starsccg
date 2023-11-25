package com.stars.maker.meta;

/**
 * 元信息异常
 *
 * @author stars
 * @version 1.0.0
 */
public class MetaException extends RuntimeException {

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
