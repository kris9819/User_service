package com.user_service.controllers;

import com.user_service.dtos.response.AuthorizeResponseDto;
import com.user_service.services.UserAuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserAuthorizationController {

    private UserAuthorizationService userAuthorizationService;

    @PostMapping(value = "/authorize")
    public AuthorizeResponseDto authorize(@RequestHeader("Authorization") String accessToken) {
        return userAuthorizationService.authorize(accessToken);
    }
}
