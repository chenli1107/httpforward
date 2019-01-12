package com.test.fwdcommon.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class BaseEntity implements Serializable{
    public static final String CLASS_NAME_FIELD = "decodeToClass";
    public static final ObjectMapper mapper = new ObjectMapper();

    //用于Json转对象
    private String decodeToClass;

    public String getDecodeToClass() {
        return this.getClass().getName();
    }

    public void setDecodeToClass(String decodeToClass) {
        this.decodeToClass = decodeToClass;
    }

}

