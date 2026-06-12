package com.app.contactmanagementsystem.service;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.app.contactmanagementsystem.service.model.User;
import com.app.contactmanagementsystem.utils.AuthUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthUtils authUtils;


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

}
