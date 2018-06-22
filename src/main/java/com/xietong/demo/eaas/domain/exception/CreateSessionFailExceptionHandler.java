package com.xietong.demo.eaas.domain.exception;

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
public class CreateSessionFailExceptionHandler {
	private final static Logger logger = Logger.getLogger(CreateSessionFailExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseDTO handlerException(HttpServletResponse response, ApplicationException e) {
		ResponseDTO errorResponse = new ResponseDTO();
		errorResponse.setSuccess(false);
		response.setStatus(500);
		CreateSessionFailException createSessionFailException = (CreateSessionFailException) e;
		errorResponse.setErrorCode(createSessionFailException.getErrorCode());
		errorResponse.setMessage(createSessionFailException.getMessage());
		logger.error(createSessionFailException);
		return errorResponse;
	}
}
