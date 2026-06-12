package com.app.contactmanagementsystem.service;

import org.springframework.stereotype.Service;

import com.app.contactmanagementsystem.model.UserEntity;
import com.app.contactmanagementsystem.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;


    public UserEntity getCurrentUser() {
        // For simplicity, we return a fixed user ID. In a real application, this would be dynamic.
        log.info("Fetching current user ID");
        return usersRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
