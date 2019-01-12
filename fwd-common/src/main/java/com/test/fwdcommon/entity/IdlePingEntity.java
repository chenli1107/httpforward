package com.test.fwdcommon.entity;

import java.util.Date;

public class IdlePingEntity extends BaseEntity{
    private String pingId;
    private Date pingTime;

    public String getPingId() {
        return pingId;
    }

    public void setPingId(String pingId) {
        this.pingId = pingId;
    }

    public Date getPingTime() {
        return pingTime;
    }

    public void setPingTime(Date pingTime) {
        this.pingTime = pingTime;
    }
}
