package com.app.contactmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.contactmanagementsystem.model.UserEntity;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

}
