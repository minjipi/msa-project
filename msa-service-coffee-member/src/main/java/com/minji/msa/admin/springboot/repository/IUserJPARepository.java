package com.minji.msa.admin.springboot.repository;

import com.minji.msa.admin.springboot.jvo.UserJVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJPARepository extends JpaRepository<UserJVO, String> {
}
