package com.user_service.controllers;

import com.user_service.dtos.request.*;
import com.user_service.dtos.response.*;
import com.user_service.services.UserAuthenticationService;
import com.user_service.services.UserAuthorizationService;
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

    private UserAuthorizationService userAuthorizationService;

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
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        ChangePasswordResponseDto changePasswordResponseDto = userAuthenticationService.changePassword(changePasswordDto);

        if (changePasswordResponseDto.changePasswordResponse() == null) {
            return new ResponseEntity<>(changePasswordResponseDto.message(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        ForgotPasswordResponseDto forgotPasswordResponseDto = userAuthenticationService.forgotPassword(forgotPasswordDto);

        if (forgotPasswordResponseDto.forgotPasswordResponse() == null) {
            return new ResponseEntity<>(forgotPasswordResponseDto.message(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Forgot password validation code send on email", HttpStatus.OK);
    }

    @PostMapping(value = "/confirmForgotPassword")
    public ResponseEntity<String> confirmForgotPassword(@RequestBody ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        ConfirmForgotPasswordResponseDto confirmForgotPasswordResponseDto = userAuthenticationService.confirmForgotPassword(confirmForgotPasswordDto);

        if (confirmForgotPasswordResponseDto.confirmForgotPasswordResponse() == null) {
            return new ResponseEntity<>(confirmForgotPasswordResponseDto.message(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Password reseted", HttpStatus.OK);
    }

//    @PostMapping(value = "/authorize")
//    public void authorize(@RequestBody AuthorizeDto authorizeDto) {
//        userAuthorizationService.authorize(authorizeDto);
//    }
}
