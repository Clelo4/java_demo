package com.chengjunjie.web.domain.model;

import com.chengjunjie.web.infrastructure.config.StatusCodeProperties;
import lombok.Data;

@Data
public class Result<T> {
    private String message;
    private int code;
    private T data;

    /**
     * 设定结果为成功
     *
     * @param msg 消息
     */
    public void setResultSuccess(String msg) {
        this.message = msg;
        this.code = StatusCodeProperties.SUCCESS;
        this.data = null;
    }

    /**
     * 设定结果为成功
     *
     * @param msg  消息
     * @param data 数据体
     */
    public void setResultSuccess(String msg, T data) {
        this.setResultSuccess(msg);
        this.data = data;
    }

    /**
     * 设定结果为失败
     *
     * @param msg 消息
     */
    public void setResultFailed(int errCode, String msg) {
        this.message = msg;
        this.code = errCode;
        this.data = null;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }
}
