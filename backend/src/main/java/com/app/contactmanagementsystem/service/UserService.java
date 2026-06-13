package com.app.contactmanagementsystem.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.app.contactmanagementsystem.controller.dto.UserDTO;
import com.app.contactmanagementsystem.mapper.UserMapper;
import com.app.contactmanagementsystem.service.model.User;
import com.app.contactmanagementsystem.utils.AuthUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthUtils authUtils;

    private final UserMapper userMapper;


    public User getCurrentUser() {
        UUID userId = UUID.fromString(authUtils.getCurrentUserId());
        String userName = authUtils.getClaim("name");
        String userEmail = authUtils.getClaim("email");

        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setEmail(userEmail);

        return user;
    }

    public UserDTO getCurrentUserDetails() {
        User currentUser = getCurrentUser();
        return userMapper.toDto(currentUser);
    }

}
