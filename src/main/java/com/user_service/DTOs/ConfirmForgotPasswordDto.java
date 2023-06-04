package com.user_service.DTOs;

import lombok.Data;

@Data
public class ConfirmForgotPasswordDto {
    String email;
    String confirmationCode;
    String password;
}
