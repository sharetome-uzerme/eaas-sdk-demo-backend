package com.xietong.demo.eaas.domain.exception;


public class CreateSessionFailException extends ApplicationException {

	private int errorCode;
	private String message;

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

	public CreateSessionFailException(Integer errorCode, String message) {
		super();
		if (errorCode != null) {
			this.errorCode = errorCode;
		}

		this.message = message;
	}

	@Override
	public String toString() {
		return "CreateSessionFailException [errorCode=" + errorCode
				+ ", message=" + message + "]";
	}

}
