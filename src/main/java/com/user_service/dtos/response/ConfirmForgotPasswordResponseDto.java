package com.user_service.dtos.response;

import lombok.Value;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;

import java.util.Optional;


public record ConfirmForgotPasswordResponseDto(Optional<ConfirmForgotPasswordResponse> confirmForgotPasswordResponse,
                                               String message) {
}
