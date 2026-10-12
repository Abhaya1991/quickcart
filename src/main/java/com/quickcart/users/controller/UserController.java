package com.quickcart.users.controller;

import com.quickcart.users.dto.LoginRequest;
import com.quickcart.users.dto.RegisterRequest;
import com.quickcart.users.dto.UserResponse;
import com.quickcart.users.repository.LoginResponse;
import com.quickcart.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
       UserResponse response = service.register(request);
       return  ResponseEntity.status(HttpStatus.CREATED)
               .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        UserResponse response = service.getUserById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }

}
