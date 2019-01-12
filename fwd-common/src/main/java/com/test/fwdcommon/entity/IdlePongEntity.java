package com.test.fwdcommon.entity;

import java.util.Date;

public class IdlePongEntity extends BaseEntity{
    private Date pongTime;
    private IdlePingEntity idelPing;

    public Date getPongTime() {
        return pongTime;
    }

    public void setPongTime(Date pongTime) {
        this.pongTime = pongTime;
    }

    public IdlePingEntity getIdelPing() {
        return idelPing;
    }

    public void setIdelPing(IdlePingEntity idelPing) {
        this.idelPing = idelPing;
    }
}
