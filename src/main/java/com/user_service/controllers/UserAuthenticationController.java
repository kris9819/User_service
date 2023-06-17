package com.user_service.controllers;

import com.user_service.dtos.request.ChangePasswordDto;
import com.user_service.dtos.request.ConfirmForgotPasswordDto;
import com.user_service.dtos.request.ConfirmSignUpDto;
import com.user_service.dtos.request.ForgotPasswordDto;
import com.user_service.dtos.request.SignInUserDto;
import com.user_service.dtos.request.SignUpUserDto;
import com.user_service.dtos.response.ChangePasswordResponseDto;
import com.user_service.dtos.response.ConfirmForgotPasswordResponseDto;
import com.user_service.dtos.response.ConfirmSignUpResponseDto;
import com.user_service.dtos.response.ForgotPasswordResponseDto;
import com.user_service.dtos.response.SignInResponseDto;
import com.user_service.dtos.response.SignUpResponseDto;
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
    public SignUpResponseDto signUpUser(@RequestBody SignUpUserDto signUpUserDto) {
        return userAuthenticationService.signUpUser(signUpUserDto);
    }

    @PostMapping(value = "/login")
    public SignInResponseDto signInUser(@RequestBody SignInUserDto signInUserDto) {
        return userAuthenticationService.signInUser(signInUserDto);
    }

    @PostMapping(value = "/confirm")
    public ConfirmSignUpResponseDto confirmSignUp(@RequestBody ConfirmSignUpDto confirmSignUpDto) {
        return userAuthenticationService.confirmSignUp(confirmSignUpDto);
    }

    @PostMapping(value = "/changePassword")
    public ChangePasswordResponseDto changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userAuthenticationService.changePassword(changePasswordDto);
    }

    @PostMapping(value = "/forgotPassword")
    public ForgotPasswordResponseDto forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return userAuthenticationService.forgotPassword(forgotPasswordDto);
    }

    @PostMapping(value = "/confirmForgotPassword")
    public ConfirmForgotPasswordResponseDto confirmForgotPassword(@RequestBody ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        return userAuthenticationService.confirmForgotPassword(confirmForgotPasswordDto);
    }
}
