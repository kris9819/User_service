package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;

@Data
@AllArgsConstructor
public class ConfirmForgotPasswordResponseDto {
    ConfirmForgotPasswordResponse confirmForgotPasswordResponse;
    String message;
}
