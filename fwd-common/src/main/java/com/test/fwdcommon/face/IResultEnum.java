package com.test.fwdcommon.face;

import com.test.fwdcommon.entity.ResultModel;
import lombok.AllArgsConstructor;

public interface IResultEnum {
    int getCode();

    String getMsg();

    /**
     * 枚举转为ResultModel
     *
     * @return
     */
    default <T> ResultModel<T> msg() {
        return new ResultModel(this.getCode(), this.getMsg(), null);
    }

    /**
     * 枚举转为ResultModel
     *
     * @param msg 自定义msg
     * @return
     */
    default <T> ResultModel<T> msg(String msg) {
        return new ResultModel(this.getCode(), msg, null);
    }

    /**
     * 枚举转为ResultModel
     *
     * @param data
     * @param <T>
     * @return
     */
    default <T> ResultModel<T> data(T data) {
        return new ResultModel(this.getCode(), this.getMsg(), data);
    }



    /**
     * 特定错误码后缀枚举，在全部项目、全部模块中含义应相同
     */
    @AllArgsConstructor
    enum ResultCodeSuffix{
        /**
         * 可控、可预期到的失败。如业务校验、参数校验等未通过等;
         */
        FAIL(1001),
        /**
         * 不可控、不可预期到的失败。如DB数据异常、未知异常等
         */
        ERROR(1002),
        ;
        public int code;
    }
}

