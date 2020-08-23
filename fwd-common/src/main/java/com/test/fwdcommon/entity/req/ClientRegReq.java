package com.test.fwdcommon.entity.req;

import com.test.fwdcommon.entity.BaseEntity;
import lombok.Data;

@Data
public class ClientRegReq extends BaseEntity {
    private String certificates;
}
