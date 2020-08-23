package com.test.httpforward.fwdserver.urlReg.bean;

import lombok.Data;

/**
 * @author chenl
 * @since 2020-08-22
 */
@Data
public class UrlRegVo {
    UrlRegDto dto;

    /**
     * 提供给客户端的凭证
     */
    String certificates;
}
