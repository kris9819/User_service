package com.user_service.dtos.response;

import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;

import java.util.Optional;


public record ChangePasswordResponseDto(Optional<ChangePasswordResponse> changePasswordResponse, String message) {
}
