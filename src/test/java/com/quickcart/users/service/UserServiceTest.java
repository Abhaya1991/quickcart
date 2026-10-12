package com.quickcart.users.service;


import com.quickcart.users.dto.LoginRequest;
import com.quickcart.users.dto.RegisterRequest;
import com.quickcart.users.dto.UserResponse;
import com.quickcart.users.entity.User;
import com.quickcart.users.exception.InvalidCredentialsException;
import com.quickcart.users.exception.ResourceNotFoundException;
import com.quickcart.users.exception.UserAlreadyExistsException;
import com.quickcart.users.repository.LoginResponse;
import com.quickcart.users.repository.UserRepository;
import com.quickcart.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldReturnUserResponse_whenRegistrationIsSuccessful() {
        RegisterRequest request = new RegisterRequest(
                "Abhaya", "abhaya@test.com", "Password123"
        );

        LocalDateTime now = LocalDateTime.now();

        User savedUser = User.builder()
                .id(1L)
                .name("Abhaya")
                .email("abhaya@test.com")
                .password("Password123")
                .createdAt(now)
                .build();

        when(repository.existsByEmail("abhaya@test.com"))
                .thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Abhaya", response.name());
        assertEquals("abhaya@test.com", response.email());

        verify(repository).existsByEmail("abhaya@test.com");
        verify(repository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "Abhaya", "abhaya@test.com", "Password123"
        );

        when(repository.existsByEmail("abhaya@test.com"))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .name("Abhaya")
                .email("abhaya@test.com")
                .password("Password123")
                .createdAt(now)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertEquals(1L, response.id());
        assertEquals("Abhaya", response.name());
        assertEquals("abhaya@test.com", response.email());
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(99L)
        );
    }

    @Test
    void login_shouldReturnSuccess_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest(
                "abhaya@test.com", "Password123"
        );

        User user = User.builder()
                .id(1L)
                .name("Abhaya")
                .email("abhaya@test.com")
                .password("Password123")
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findByEmail("abhaya@test.com"))
                .thenReturn(Optional.of(user));

        LoginResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("Login successful", response.message());
        assertEquals(1L, response.user().id());
    }

    @Test
    void login_shouldThrowException_whenEmailDoesNotExist() {
        LoginRequest request = new LoginRequest(
                "unknown@test.com", "Password123"
        );

        when(repository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );
    }

    @Test
    void login_shouldThrowException_whenPasswordIsIncorrect() {
        LoginRequest request = new LoginRequest(
                "abhaya@test.com", "WrongPassword"
        );

        User user = User.builder()
                .id(1L)
                .name("Abhaya")
                .email("abhaya@test.com")
                .password("Password123")
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findByEmail("abhaya@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );
    }
}

