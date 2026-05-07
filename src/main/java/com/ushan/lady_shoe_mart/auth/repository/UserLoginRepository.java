package com.ushan.lady_shoe_mart.auth.repository;

import com.ushan.lady_shoe_mart.auth.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long>, JpaSpecificationExecutor<UserLogin> {

    UserLogin findByTokenKey(String tokenKey);
    boolean existsByTokenKey(String tokenKey);
    List<UserLogin> findAllByUserIdAndTokenExpired(Long userId, Boolean tokenExpired);
}
