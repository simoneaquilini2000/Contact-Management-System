package com.app.contactmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.contactmanagementsystem.controller.dto.UserDTO;
import com.app.contactmanagementsystem.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    public ResponseEntity<UserDTO> getUserDetails() {
        log.info("Fetching user details");
        UserDTO userDetails = userService.getCurrentUserDetails();
        return ResponseEntity.ok(userDetails);
    }
}
