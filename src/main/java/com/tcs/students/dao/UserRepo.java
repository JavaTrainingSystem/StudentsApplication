package com.tcs.students.dao;

import com.tcs.students.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String userName);


}
