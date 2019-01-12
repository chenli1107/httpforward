package com.test.fwdcommon.entity;

import com.fasterxml.jackson.core.JsonProcessingException;

public class HttpUrlMappingResult extends BaseEntity{
    private boolean success;
    private String msg;
    private HttpUrlMapping mapping;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HttpUrlMapping getMapping() {
        return mapping;
    }

    public void setMapping(HttpUrlMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
