package com.quickcart.users;

import com.quickcart.users.dto.LoginRequest;
import com.quickcart.users.dto.RegisterRequest;
import com.quickcart.users.dto.UserResponse;
import com.quickcart.users.entity.User;
import com.quickcart.users.exception.InvalidCredentialsException;
import com.quickcart.users.exception.ResourceNotFoundException;
import com.quickcart.users.exception.UserAlreadyExistsException;
import com.quickcart.users.repository.LoginResponse;
import com.quickcart.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserResponse register(RegisterRequest request){
        if (repository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = repository.save(user);
        return  new UserResponse(savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt());
    }

    public UserResponse getUserById(Long userId){
      User response =  repository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Available"));
        return new UserResponse(response.getId(),
                response.getName(),
                response.getEmail(),
                response.getCreatedAt());
    }
    public LoginResponse login(LoginRequest request) {

        User user = repository.findByEmail(request.email())
                .orElseThrow(() ->
                 new InvalidCredentialsException(
                   "Invalid email or password"
                 )
                );

        if (!user.getPassword().equals(request.password())) {
             throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );

        return new LoginResponse(
                "Login successful",
                userResponse
        );
    }

}
