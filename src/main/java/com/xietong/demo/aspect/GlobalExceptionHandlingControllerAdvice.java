package com.xietong.demo.aspect;

import com.xietong.demo.eaas.facade.dto.ResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandlingControllerAdvice {

	private final static Logger logger = Logger.getLogger(GlobalExceptionHandlingControllerAdvice.class);

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseDTO handleError(HttpServletResponse response, Exception exception) throws Exception {
		ResponseDTO errorResponse = new ResponseDTO();
		errorResponse.setSuccess(false);
		response.setStatus(500);// internal server error
		logger.error("Ops!", exception);
		return errorResponse;
	}
}
