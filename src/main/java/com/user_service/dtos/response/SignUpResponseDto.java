package com.user_service.dtos.response;

public record SignUpResponseDto(String email,
                                String deliveryMedium,
                                String userSub) {
}
