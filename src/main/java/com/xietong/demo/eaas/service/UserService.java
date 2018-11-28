package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.facade.dto.SessionUserDTO;

import java.util.List;

public interface UserService {
	/**
	 * @param searchKey
	 * @return
	 */
	List<SessionUserDTO> queryByKey(String searchKey);

	List<SessionUserDTO> queryByIds(String ids);

	SessionUserDTO findUserByKey(String key);

}
