package com.xietong.demo.util;

import com.xietong.demo.eaas.facade.dto.ResponseDTO;

public class ResponseUtil {
	public static ResponseDTO buildResponseDTO(Object data){
		ResponseDTO result = new ResponseDTO();
		result.setData(data);
		return result;
	}
}
