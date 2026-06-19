package com.app.contactmanagementsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.contactmanagementsystem.controller.dto.UserDTO;
import com.app.contactmanagementsystem.mapper.UserMapper;
import com.app.contactmanagementsystem.service.model.User;
import com.app.contactmanagementsystem.utils.AuthUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthUtils authUtils;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private String userIdString;
    private String userName;
    private String userEmail;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userIdString = userId.toString();
        userName = "Jane Doe";
        userEmail = "jane.doe@example.com";
    }

    @Test
    void getCurrentUser_shouldReturnUserWithCorrectFields_whenClaimsArePresent() {
        when(authUtils.getCurrentUserId()).thenReturn(userIdString);
        when(authUtils.getClaim("name")).thenReturn(userName);
        when(authUtils.getClaim("email")).thenReturn(userEmail);

        User result = userService.getCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo(userName);
        assertThat(result.getEmail()).isEqualTo(userEmail);

        verify(authUtils).getCurrentUserId();
        verify(authUtils).getClaim("name");
        verify(authUtils).getClaim("email");
        verifyNoMoreInteractions(authUtils);
    }

    @Test
    void getCurrentUser_shouldThrowException_whenUserIdIsNotValidUUID() {
        when(authUtils.getCurrentUserId()).thenReturn("not-a-valid-uuid");

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getCurrentUser_shouldThrowException_whenUserIdIsNull() {
        when(authUtils.getCurrentUserId()).thenReturn(null);

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getCurrentUser_shouldReturnUserWithNullFields_whenClaimsAreNull() {
        when(authUtils.getCurrentUserId()).thenReturn(userIdString);
        when(authUtils.getClaim("name")).thenReturn(null);
        when(authUtils.getClaim("email")).thenReturn(null);

        User result = userService.getCurrentUser();

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isNull();
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void getCurrentUserDetails_shouldReturnMappedDto_whenUserExists() {
        when(authUtils.getCurrentUserId()).thenReturn(userIdString);
        when(authUtils.getClaim("name")).thenReturn(userName);
        when(authUtils.getClaim("email")).thenReturn(userEmail);

        UserDTO expectedDto = new UserDTO();
        expectedDto.setName(userName);
        expectedDto.setEmail(userEmail);

        when(userMapper.toDto(org.mockito.ArgumentMatchers.any(User.class)))
                .thenReturn(expectedDto);

        UserDTO result = userService.getCurrentUserDetails();

        assertThat(result).isEqualTo(expectedDto);
        verify(userMapper).toDto(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void getCurrentUserDetails_shouldPropagateException_whenUserIdIsInvalid() {
        when(authUtils.getCurrentUserId()).thenReturn("invalid-uuid");

        assertThatThrownBy(() -> userService.getCurrentUserDetails())
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoMoreInteractions(userMapper);
    }
}
