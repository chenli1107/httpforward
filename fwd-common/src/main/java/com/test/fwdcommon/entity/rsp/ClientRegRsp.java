package com.test.fwdcommon.entity.rsp;

import com.test.fwdcommon.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRegRsp extends BaseEntity {
    private boolean success;
    private String msg;
    private String clientWebServerPath; // ip:port或ip:port/path 客户端web服务地址前缀，访问代理时，将会替换为此前缀路径
}
