package com.xietong.demo.eaas.facade.dto;

import java.io.Serializable;

//@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class ResponseDTO implements Serializable {
    private boolean success;
    private int errorCode;
    private String message;
    private Object data;

    public ResponseDTO() {
        this.success = true;
    }

    public ResponseDTO(Object data) {
        this.success = true;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
