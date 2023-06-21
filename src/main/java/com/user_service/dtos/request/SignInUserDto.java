package com.user_service.dtos.request;

import lombok.Value;


public record SignInUserDto(String email, String password) {

}
