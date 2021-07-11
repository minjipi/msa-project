package com.minji.msa.member.springboot.repository;

import com.minji.msa.member.springboot.jvo.UserJVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUserJPARepository extends JpaRepository<UserJVO, String> {
    Optional<UserJVO> findByUserName(String userName);
}
