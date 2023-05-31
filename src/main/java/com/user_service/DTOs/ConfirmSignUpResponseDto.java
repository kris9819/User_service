package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;

@Data
@AllArgsConstructor
public class ConfirmSignUpResponseDto {
    ConfirmSignUpResponse confirmSignUpResponse;
    String message;
}
