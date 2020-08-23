package com.test.httpforward.fwdserver.urlReg.service;

import com.test.fwdcommon.entity.ResultModel;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegDto;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegVo;

import java.util.Collection;
import java.util.List;

/**
 * @author chenl
 * @since 2020-08-23
 */
public interface IUrlRegService {

    UrlRegVo getByProxyServerPath(String proxyServerPath);

    UrlRegVo getByCertificates(String certificates);

    /**
     * 注册
     * @param dto
     * @return
     */
    ResultModel<UrlRegVo> regist(UrlRegDto dto);

    /**
     * 注销
     * @param certificates
     */
    void cancel(String certificates);

    Collection<UrlRegVo> getAll();
}
