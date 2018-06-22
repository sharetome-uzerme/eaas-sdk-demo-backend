package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.facade.dto.AuthResult;

public interface AuthService {
	/**
	 * Auth by userName and password.
	 * @param userName
	 * @param password
	 * @return
	 */
	AuthResult auth(String userName,String password);
}
