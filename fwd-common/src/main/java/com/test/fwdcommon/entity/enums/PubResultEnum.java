package com.test.fwdcommon.entity.enums;

import com.test.fwdcommon.entity.ResultModel;
import com.test.fwdcommon.face.IResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenl
 * @since 2020-07-04
 */
@AllArgsConstructor
@Getter
public enum PubResultEnum implements IResultEnum {
    /**
     * 通用错误码
     */
    SUCC(ResultModel.DEFAULT_SUCC_CODE, "成功"),
    FAIL(PubResultEnum.MODULE_NO + ResultCodeSuffix.FAIL.code, "失败"),
    ERROR(PubResultEnum.MODULE_NO + ResultCodeSuffix.ERROR.code, "失败"),

    /**
     * 其他错误码
     */
    FAAWWEXX_XXAFA(10002001, "SDFADSFA"),
    XXFA_XXXADFA(10002002, "FADSFADSCF"),

    ;

    private int code;
    private String msg;

    /**
     * 模块code
     */
    private static final int MODULE_NO = 10000000;
}
