package com.user_service.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetails {

    Long userId;
    String name;
    String email;
    LocalDate birthdate;
    String password;
}
