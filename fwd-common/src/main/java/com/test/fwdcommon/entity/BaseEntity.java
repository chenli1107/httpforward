package com.test.fwdcommon.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable{
    public static final String CLASS_NAME_FIELD = "decodeToClass";

    //用于Json转对象
    private String decodeToClass;

    public String getDecodeToClass() {
        return this.getClass().getName();
    }
}

