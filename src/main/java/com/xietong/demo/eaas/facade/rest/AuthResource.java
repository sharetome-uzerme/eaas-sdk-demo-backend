package com.xietong.demo.eaas.facade.rest;

import com.xietong.demo.eaas.facade.dto.AuthResult;
import com.xietong.demo.eaas.service.AuthService;
import com.xietong.phoenix.common.util.ParamValidateUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于EAAS获取第三方系统(demo service)的token.
 * 支持token和匿名token. 
 * 
 */
@Controller
public class AuthResource {
	private final static Logger logger = Logger.getLogger(AuthResource.class);

	@Autowired
	private AuthService authService;

	/**
	 * 登录demo service获取token. 验证用户名 密码， 返回token.
	 * 对应于mongodb中client_configuration的authenticationServiceUrl回调地址
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/auth/token", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "登录", notes = "返回值为 code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "有权限"), @ApiResponse(code = 401, message = "无权限") })
	public AuthResult login(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@ApiParam(name = "userName", value = "userName") @RequestParam(value = "userName") String userName,
			@ApiParam(name = "password", value = "password") @RequestParam(value = "password") String password)
			throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("auth request, username:"+userName);
		}

		ParamValidateUtils.checkNotNull(userName, "userName is required");
		ParamValidateUtils.checkNotNull(password, "password is required");

		AuthResult authResult = authService.auth(userName, password);

		return authResult;
	}

}
