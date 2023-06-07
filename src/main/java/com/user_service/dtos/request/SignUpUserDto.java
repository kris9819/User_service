package com.user_service.dtos.request;

import lombok.Value;

import java.time.LocalDate;


public record SignUpUserDto(String name, String email, LocalDate birthdate, String password, String passwordRepeat) {

}
