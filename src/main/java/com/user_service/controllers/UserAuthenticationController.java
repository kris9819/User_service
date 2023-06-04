package com.user_service.controllers;

import com.user_service.DTOs.*;
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
    public ResponseEntity<String> signUpUser(@RequestBody SignUpUserDto signUpUserDto) {
        SignUpResponseDto signUpResponseDto = userAuthenticationService.signUpUser(signUpUserDto);

        if (signUpResponseDto.getSignUpResponse() == null) {
            return new ResponseEntity<>(signUpResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Account sign up, confirmation code sent to " + signUpUserDto.getEmail(), HttpStatus.OK);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<String> confirmSignUp(@RequestBody ConfirmRegisterDto confirmRegisterDto) {
        ConfirmSignUpResponseDto confirmSignUpResponseDto = userAuthenticationService.confirmSignUp(confirmRegisterDto);

        if (confirmSignUpResponseDto.getConfirmSignUpResponse() == null) {
            return new ResponseEntity<>(confirmSignUpResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Account confirmed successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> signInUser(@RequestBody SignInUserDto signInUserDto) {
        SignInResponseDto signInResponseDto = userAuthenticationService.signInUser(signInUserDto);

        if (signInResponseDto.getAdminInitiateAuthResponse() == null) {
            return new ResponseEntity<>(signInResponseDto.getMesssage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        ChangePasswordResponseDto changePasswordResponseDto = userAuthenticationService.changePassword(changePasswordDto);

        if (changePasswordResponseDto.getChangePasswordResponse() == null) {
            return new ResponseEntity<>(changePasswordResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        ForgotPasswordResponseDto forgotPasswordResponseDto = userAuthenticationService.forgotPassword(forgotPasswordDto);

        if (forgotPasswordResponseDto.getForgotPasswordResponse() == null) {
            return new ResponseEntity<>(forgotPasswordResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Forgot password validation code send on email", HttpStatus.OK);
    }

    @PostMapping(value = "/confirmForgotPassword")
    public ResponseEntity<String> confirmForgotPassword(@RequestBody ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        ConfirmForgotPasswordResponseDto confirmForgotPasswordResponseDto = userAuthenticationService.confirmForgotPassword(confirmForgotPasswordDto);

        if (confirmForgotPasswordResponseDto.getConfirmForgotPasswordResponse() == null) {
            return new ResponseEntity<>(confirmForgotPasswordResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Password reseted", HttpStatus.OK);
    }

    @PostMapping(value = "/authorize")
    public void authorize(@RequestBody AuthorizeDto authorizeDto) {
        userAuthenticationService.authorize(authorizeDto);
    }
}
