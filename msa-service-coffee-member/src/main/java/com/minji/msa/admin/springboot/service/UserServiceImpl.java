package com.minji.msa.admin.springboot.service;

import com.minji.msa.admin.domain.repository.IUserRepository;
import com.minji.msa.admin.domain.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserService {

    public UserServiceImpl(IUserRepository iUserRepository) {

        super(iUserRepository);
    }
}