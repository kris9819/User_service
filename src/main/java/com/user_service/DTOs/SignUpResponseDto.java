package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

@Data
@AllArgsConstructor
public class SignUpResponseDto {
    SignUpResponse signUpResponse;
    String message;
}
