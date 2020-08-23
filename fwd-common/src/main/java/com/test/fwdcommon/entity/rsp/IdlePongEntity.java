package com.test.fwdcommon.entity.rsp;

import com.test.fwdcommon.entity.BaseEntity;
import com.test.fwdcommon.entity.req.IdlePingEntity;
import lombok.Data;

import java.util.Date;

@Data
public class IdlePongEntity extends BaseEntity {
    private Date pongTime;
    private IdlePingEntity idelPing;
}
