package com.user_service.dtos.response;

public record ErrorResponseDto(String resource,
                               String code,
                               String message) {
}
