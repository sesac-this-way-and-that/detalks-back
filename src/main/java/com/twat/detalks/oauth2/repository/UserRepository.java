package com.twat.detalks.oauth2.repository;

import com.twat.detalks.oauth2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String Username);
}
