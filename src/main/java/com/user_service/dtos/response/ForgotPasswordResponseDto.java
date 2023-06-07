package com.user_service.dtos.response;

import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;

import java.util.Optional;


public record ForgotPasswordResponseDto(Optional<ForgotPasswordResponse> forgotPasswordResponse, String message) {
}
