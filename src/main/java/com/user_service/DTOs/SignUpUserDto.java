package com.user_service.DTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpUserDto {

    String name;
    String email;
    LocalDate birthdate;
    String password;
    String passwordRepeat;
}
