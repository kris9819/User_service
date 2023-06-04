package com.user_service.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.user_service.DTOs.*;
import com.user_service.utility.AwsCognitoRSAKeyProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class UserAuthenticationService {

    private CognitoIdentityClient cognitoIdentityClient;

    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public static final String CLIENT_ID = "1j3sjiopi69flk3nt6g58cnspa";
    public static final String USERPOOL_ID = "eu-north-1_pua0XpF66";

    public SignUpResponseDto signUpUser(SignUpUserDto signUpUserDto) {
        AttributeType attributeType = AttributeType.builder()
                .name("email")
                .value(signUpUserDto.getEmail())
                .build();

        AttributeType attributeType1 = AttributeType.builder()
                .name("name")
                .value(signUpUserDto.getName())
                .build();

        AttributeType attributeType2 = AttributeType.builder()
                .name("birthdate")
                .value(signUpUserDto.getBirthdate().toString())
                .build();

        List<AttributeType> attrs = new ArrayList<>();
        attrs.add(attributeType);
        attrs.add(attributeType1);
        attrs.add(attributeType2);
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(signUpUserDto.getEmail())
                    .clientId(CLIENT_ID)
                    .password(signUpUserDto.getPassword())
                    .build();

            log.info("New user with email: {} created", signUpUserDto.getEmail());
            return new SignUpResponseDto(cognitoIdentityProviderClient.signUp(signUpRequest), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to sign up user, error: {}", e.awsErrorDetails().errorMessage());
            return new SignUpResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public SignInResponseDto signInUser(SignInUserDto signInUserDto) {
        try {
            Map<String, String> authParams = new LinkedHashMap<>() {{
                put("USERNAME", signInUserDto.getEmail());
                put("PASSWORD", signInUserDto.getPassword());
            }};

            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .clientId(CLIENT_ID)
                    .userPoolId(USERPOOL_ID)
                    .authParameters(authParams)
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .build();

            log.info("User {} signed in", signInUserDto.getEmail());
            return new SignInResponseDto(cognitoIdentityProviderClient.adminInitiateAuth(authRequest), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to sign in user, error: {}", e.awsErrorDetails().errorMessage());
            return new SignInResponseDto(null, e.awsErrorDetails().errorMessage());
        } finally {
            cognitoIdentityClient.close();
        }
    }

    public ConfirmSignUpResponseDto confirmSignUp(ConfirmRegisterDto confirmRegisterDto) {
        try {
            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(CLIENT_ID)
                    .confirmationCode(confirmRegisterDto.getCode())
                    .username(confirmRegisterDto.getEmail())
                    .build();

            log.info("User {} sign up confirmed", confirmRegisterDto.getEmail());
            return new ConfirmSignUpResponseDto(cognitoIdentityProviderClient.confirmSignUp(signUpRequest), confirmRegisterDto.getEmail() + " was confirmed");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to confirm user sign up, error: {}", e.awsErrorDetails().errorMessage());
            return new ConfirmSignUpResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public ChangePasswordResponseDto changePassword(ChangePasswordDto changePasswordDto) {
        try {
            ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                    .accessToken(changePasswordDto.getAccessToken())
                    .previousPassword(changePasswordDto.getOldPassword())
                    .proposedPassword(changePasswordDto.getNewPassword())
                    .build();

            log.info("User password changed");
            return new ChangePasswordResponseDto(cognitoIdentityProviderClient.changePassword(changePasswordRequest), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to change user password, error: {}", e.awsErrorDetails().errorMessage());
            return new ChangePasswordResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        try {
            ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                    .clientId(CLIENT_ID)
                    .username(forgotPasswordDto.getEmail())
                    .build();

            log.info("User {} password restart process started", forgotPasswordDto.getEmail());
            return new ForgotPasswordResponseDto(cognitoIdentityProviderClient.forgotPassword(forgotPasswordRequest), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to start forgot password process, error: {}", e.awsErrorDetails().errorMessage());
            return new ForgotPasswordResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public ConfirmForgotPasswordResponseDto confirmForgotPassword(ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        try {
            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                    .clientId(CLIENT_ID)
                    .username(confirmForgotPasswordDto.getEmail())
                    .confirmationCode(confirmForgotPasswordDto.getConfirmationCode())
                    .password(confirmForgotPasswordDto.getPassword())
                    .build();

            log.info("User {} password reseted successfully", confirmForgotPasswordDto.getEmail());
            return new ConfirmForgotPasswordResponseDto(cognitoIdentityProviderClient.confirmForgotPassword(confirmForgotPasswordRequest), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to reset user password, error: {}", e.awsErrorDetails().errorMessage());
            return new ConfirmForgotPasswordResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public void authorize(AuthorizeDto authorizeDto) {
        RSAKeyProvider rsaKeyProvider = new AwsCognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .build();
        jwtVerifier.verify(authorizeDto.getToken());
    }
}
