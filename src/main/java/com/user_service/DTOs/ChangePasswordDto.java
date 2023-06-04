package com.user_service.DTOs;

import lombok.Data;

@Data
public class ChangePasswordDto {
    String accessToken;
    String oldPassword;
    String newPassword;
}
