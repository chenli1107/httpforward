package com.test.fwdcommon.entity.req;

import com.test.fwdcommon.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@Builder
public class HttpPackageReq extends BaseEntity {
    private String url;
    private String methodName;
    private List<Map.Entry<String, String>> headers;
    private byte[] contentBytes;
    private String requestId;
    private Date requestTime;
}
