package com.app.contactmanagementsystem.mapper;

import org.springframework.stereotype.Component;
import com.app.contactmanagementsystem.controller.dto.UserDTO;
import com.app.contactmanagementsystem.service.model.User;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
