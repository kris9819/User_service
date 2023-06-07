package com.user_service.dtos.request;

import lombok.Value;


public record ConfirmForgotPasswordDto(String email, String confirmationCode, String password) {
}
