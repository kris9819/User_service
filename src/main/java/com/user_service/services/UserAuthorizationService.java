package com.user_service.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.user_service.dtos.request.AuthorizeDto;
import com.user_service.utility.AwsCognitoRSAKeyProvider;

public class UserAuthorizationService {

    public void authorize(AuthorizeDto authorizeDto) {
        RSAKeyProvider rsaKeyProvider = new AwsCognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .build();
        jwtVerifier.verify(authorizeDto.token());
    }
}
