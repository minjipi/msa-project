package com.minji.msa.member.springboot.service;

import com.minji.msa.member.domain.repository.IUserRepository;
import com.minji.msa.member.domain.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserService {

    public UserServiceImpl(IUserRepository iUserRepository) {

        super(iUserRepository);
    }
}