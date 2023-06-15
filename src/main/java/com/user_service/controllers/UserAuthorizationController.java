package com.user_service.controllers;

import com.user_service.dtos.request.AuthorizeDto;
import com.user_service.dtos.response.AuthorizeResponseDto;
import com.user_service.services.UserAuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserAuthorizationController {

    private UserAuthorizationService userAuthorizationService;

    @PostMapping(value = "/authorize")
    public ResponseEntity<AuthorizeResponseDto> authorize(@RequestBody AuthorizeDto authorizeDto) {
        return new ResponseEntity<>(userAuthorizationService.authorize(authorizeDto), HttpStatus.OK);
    }
}
