package com.test.fwdcommon.entity.req;

import com.test.fwdcommon.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class IdlePingEntity extends BaseEntity {
    private String pingId;
    private Date pingTime;
}
