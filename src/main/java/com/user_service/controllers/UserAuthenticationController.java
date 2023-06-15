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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserAuthenticationController {

    private UserAuthenticationService userAuthenticationService;

    @PostMapping(value = "/register")
    public ResponseEntity<SignUpResponseDto> signUpUser(@RequestBody SignUpUserDto signUpUserDto) {
        return new ResponseEntity<>(userAuthenticationService.signUpUser(signUpUserDto), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<SignInResponseDto> signInUser(@RequestBody SignInUserDto signInUserDto) {
        return new ResponseEntity<>(userAuthenticationService.signInUser(signInUserDto), HttpStatus.OK);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<ConfirmSignUpResponseDto> confirmSignUp(@RequestBody ConfirmSignUpDto confirmSignUpDto) {
        return new ResponseEntity<>(userAuthenticationService.confirmSignUp(confirmSignUpDto), HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return new ResponseEntity<>(userAuthenticationService.changePassword(changePasswordDto), HttpStatus.OK);
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return new ResponseEntity<>(userAuthenticationService.forgotPassword(forgotPasswordDto), HttpStatus.OK);
    }

    @PostMapping(value = "/confirmForgotPassword")
    public ResponseEntity<ConfirmForgotPasswordResponseDto> confirmForgotPassword(@RequestBody ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        return new ResponseEntity<>(userAuthenticationService.confirmForgotPassword(confirmForgotPasswordDto), HttpStatus.OK);
    }
}
