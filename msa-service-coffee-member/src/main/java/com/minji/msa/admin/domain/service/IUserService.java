package com.minji.msa.admin.domain.service;

import com.minji.msa.admin.domain.vo.UserVO;

import java.util.List;

public interface IUserService {

	long getAllUserCount();
	List<UserVO> getAllUser(String page, String size, String sort, String sortKey);

	UserVO getUserById(UserVO userVO);
	UserVO addUser(UserVO userVO);
	UserVO modifyUser(UserVO userVO);
	void removeUser(List<UserVO> userVOList);
}