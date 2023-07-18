package com.user_service.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.user_service.dtos.response.AuthorizeResponseDto;
import com.user_service.utility.ExternalCognitoClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserAuthorizationService {

    private RSAKeyProvider rsaKeyProvider;

    private ExternalCognitoClient externalCognitoClient;

    public AuthorizeResponseDto authorize(String accessToken) {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .build();
            jwtVerifier.verify(accessToken);

            Optional<GetUserResponse> response = externalCognitoClient.getUserInfo(accessToken);
            if (response.isPresent()) {
                GetUserResponse responseValue = response.get();
                String username = responseValue.username();
                String name = responseValue.userAttributes().get(3).value();
                String email = responseValue.userAttributes().get(4).value();
                return new AuthorizeResponseDto(username, email, name);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }
}
