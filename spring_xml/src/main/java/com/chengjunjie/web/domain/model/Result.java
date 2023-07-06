package com.chengjunjie.web.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Result<T> implements Serializable {
    private String message;
    private boolean success;
    private T data;

    /**
     * 设定结果为成功
     *
     * @param msg 消息
     */
    public void setResultSuccess(String msg) {
        this.message = msg;
        this.success = true;
        this.data = null;
    }

    /**
     * 设定结果为成功
     *
     * @param msg  消息
     * @param data 数据体
     */
    public void setResultSuccess(String msg, T data) {
        this.message = msg;
        this.success = true;
        this.data = data;
    }

    /**
     * 设定结果为失败
     *
     * @param msg 消息
     */
    public void setResultFailed(String msg) {
        this.message = msg;
        this.success = false;
        this.data = null;
    }
}
