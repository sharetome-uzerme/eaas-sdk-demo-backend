package com.xietong.demo.eaas.domain.exception;

/**
 * @author Guitar
 */
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = -6485405088144036307L;

    protected int errorCode = 500;
    private Exception ex;

    private String errorMessage;


    public String getErrorMessage() {
        return errorMessage;
    }

    public ApplicationException() {
    }
    public ApplicationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ApplicationException(Throwable t) {
        super(t);
    }

    public ApplicationException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }
    public ApplicationException(int errorCode, Exception ex, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.ex=ex;
    }

    public ApplicationException(int errorCode, String msg, Throwable t) {
        super(msg, t);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
    @Override
    public String toString() {
        return "errorMessage='" + errorMessage + '\''
                + " errorCode='" +  errorCode + "\'";
    }

}
