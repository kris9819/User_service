package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;

@Data
@AllArgsConstructor
public class ChangePasswordResponseDto {
    ChangePasswordResponse changePasswordResponse;
    String message;
}
