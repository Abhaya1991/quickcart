package com.quickcart.users.repository;

import com.quickcart.users.dto.UserResponse;

public record LoginResponse(
        String message,
        UserResponse user
) {
}
