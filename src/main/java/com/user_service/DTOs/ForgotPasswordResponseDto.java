package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;

@Data
@AllArgsConstructor
public class ForgotPasswordResponseDto {
    ForgotPasswordResponse forgotPasswordResponse;
    String message;
}
