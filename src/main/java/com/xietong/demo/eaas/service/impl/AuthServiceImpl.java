package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.facade.dto.AuthResult;
import com.xietong.demo.eaas.service.AuthService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final static Logger logger = Logger.getLogger(AuthServiceImpl.class);

	@Value("${eaas.demo.userId}")
	private String userId;

	@Value("${eaas.demo.password}")
	private String password;


	@Override
	public AuthResult auth(String userName, String password) {
		if (!authSuccess(userName, password)) {
			return AuthResult.AUTH_FAIL;
		}

		AuthResult result = buildAuthResult();

		return result;
	}

	private AuthResult buildAuthResult() {
		AuthResult result = new AuthResult();
		result.setToken(RandomStringUtils.randomAlphanumeric(20));
		result.setSuccess(true);
		return result;
	}

	private boolean authSuccess(String userName, String password) {
		if(logger.isDebugEnabled()) {
			logger.info("userName: " + userName + " == password: " + password);
		}
		if (userId.equals(userName) && password.equals(password)) {
			return true;
		}
		return false;
	}
}
