package com.xietong.demo.eaas.facade.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class AuthResult {
	public static final AuthResult AUTH_FAIL= new AuthResult();
	
	private Boolean success;
	@JsonProperty("access_token")
	private String token;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
