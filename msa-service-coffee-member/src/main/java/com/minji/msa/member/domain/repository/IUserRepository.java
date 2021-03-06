package com.minji.msa.member.domain.repository;

import com.minji.msa.member.domain.entity.UserEntity;

import java.util.List;

public interface IUserRepository {
	long getAllUserCount();
	List<UserEntity> getAllUser(String page, String size, String sort, String sortKey);
	UserEntity getUserByUserName(UserEntity userEntity);
	UserEntity addUser(UserEntity userEntity);
	UserEntity modifyUser(UserEntity userEntity);
	void removeUser(List<UserEntity> userEntity);
}
