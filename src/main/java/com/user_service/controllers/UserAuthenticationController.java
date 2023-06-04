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
    public ResponseEntity<String> signUpUser(@RequestBody RegisterUserDto registerUserDto) {
        SignUpResponseDto signUpResponseDto = userAuthenticationService.signUpUser(registerUserDto);

        if (signUpResponseDto.getSignUpResponse() == null) {
            return new ResponseEntity<String>(signUpResponseDto.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Account sign up, confirmation code sent to " + registerUserDto.getEmail(), HttpStatus.OK);
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
    public void signInUser(@RequestBody LoginUserDto loginUserDto) {
        userAuthenticationService.signInUser(loginUserDto);
    }
}
