package com.user_service.controllers;

import com.user_service.DTOs.LoginUserDto;
import com.user_service.DTOs.RegisterUserDto;
import com.user_service.services.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserAuthenticationController {

    private UserAuthenticationService userAuthenticationService;

    @PostMapping(value = "/register")
    public void registerUser(@RequestBody RegisterUserDto registerUserDto) {
        userAuthenticationService.printUserDetails(registerUserDto);
    }

    @PostMapping(value = "/login")
    public void loginUser(@RequestBody LoginUserDto loginUserDto) {
        userAuthenticationService.printUserDetails(loginUserDto);
    }
}
