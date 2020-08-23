package com.test.fwdcommon.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;

/**
 * @Author 楚休红
 * @Date 2019/7/16
 */
@Data
public class ResultModel<T> implements Serializable {
    /**
     * 成功
     */
    public static final int DEFAULT_SUCC_CODE = 0;
    private int code;
    private String msg;
    private T data;
    private final long timestamp = System.currentTimeMillis();

    public ResultModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * copy  ResultModel的code msg,返回新的ResultModel对象
     */
    public <T> ResultModel<T> copyResultMsg() {
        return new ResultModel(this.getCode(), this.getMsg(), null);
    }

    public static <T> ResultModel<T> succData(T data) {
        return new ResultModel(DEFAULT_SUCC_CODE, "成功", data);
    }

    /**
     * 是否成功
     */
    @Transient
    public boolean isSuccess() {
        return this.code == DEFAULT_SUCC_CODE;
    }

    public ResultModel<T> setData(T data) {
        this.data = data;
        return this;
    }
}
