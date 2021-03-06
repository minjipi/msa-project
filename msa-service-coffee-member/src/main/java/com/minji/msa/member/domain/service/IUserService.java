package com.minji.msa.member.domain.service;

import com.minji.msa.member.domain.vo.UserVO;

import java.util.List;

public interface IUserService {

	long getAllUserCount();
	List<UserVO> getAllUser(String page, String size, String sort, String sortKey);

	UserVO getUserByUserName(UserVO userVO);
	UserVO addUser(UserVO userVO);
	UserVO modifyUser(UserVO userVO);
	void removeUser(List<UserVO> userVOList);
}