package com.user_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;

@Data
@AllArgsConstructor
public class SignInResponseDto {
    AdminInitiateAuthResponse adminInitiateAuthResponse;
    String messsage;
}
