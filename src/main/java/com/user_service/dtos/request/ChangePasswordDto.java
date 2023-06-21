package com.user_service.dtos.request;

public record ChangePasswordDto(String accessToken, String oldPassword, String newPassword) {
}
