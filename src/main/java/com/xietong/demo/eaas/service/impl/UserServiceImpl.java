package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.facade.dto.SessionUserDTO;
import com.xietong.demo.eaas.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public List<SessionUserDTO> queryByKey(String searchKey) {
		List<SessionUserDTO> users = new ArrayList<>();

		SessionUserDTO dto = new SessionUserDTO();
		dto.setNickName("demoTest");
		dto.setIsAnonymous(false);
		users.add(dto);
		return users;
	}

	@Override
	public List<SessionUserDTO> queryByIds(String ids) {
		List<String> idList = Arrays.asList(ids.split("\\s*,\\s*"));
		if (CollectionUtils.isNotEmpty(idList)) {

		}
		List<SessionUserDTO> users = new ArrayList<>();
		SessionUserDTO dto = new SessionUserDTO();
		dto.setNickName("demoTest");
		dto.setIsAnonymous(false);
		users.add(dto);
		return users;
	}

	@Override
	public SessionUserDTO findUserByKey(String key) {
		SessionUserDTO dto = new SessionUserDTO();
		dto.setNickName("demoTest");
		dto.setIsAnonymous(false);
		return dto;
	}

}
