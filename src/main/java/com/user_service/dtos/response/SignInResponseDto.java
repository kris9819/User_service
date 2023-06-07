package com.user_service.dtos.response;

public record SignInResponseDto(String accessToken,
                                String tokenType,
                                String refreshToken,
                                String idToken,
                                Integer expiresIn) {
}
