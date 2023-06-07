package com.user_service.services;


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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserAuthenticationService {

    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public static final String CLIENT_ID = "1j3sjiopi69flk3nt6g58cnspa";
    public static final String USERPOOL_ID = "eu-north-1_pua0XpF66";

    public SignUpResponseDto signUpUser(SignUpUserDto signUpUserDto) {
        AttributeType attributeType = AttributeType.builder()
                .name("email")
                .value(signUpUserDto.email())
                .build();

        AttributeType attributeType1 = AttributeType.builder()
                .name("name")
                .value(signUpUserDto.name())
                .build();

        AttributeType attributeType2 = AttributeType.builder()
                .name("birthdate")
                .value(signUpUserDto.birthdate().toString())
                .build();

        List<AttributeType> attrs = new ArrayList<>();
        attrs.add(attributeType);
        attrs.add(attributeType1);
        attrs.add(attributeType2);
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(signUpUserDto.email())
                    .clientId(CLIENT_ID)
                    .password(signUpUserDto.password())
                    .build();

            Optional<SignUpResponse> response = Optional.ofNullable(cognitoIdentityProviderClient.signUp(signUpRequest));

            if (response.isPresent()) {
                log.info("New user with email: {} created", signUpUserDto.email());
                return new SignUpResponseDto(response.get().codeDeliveryDetails().destination(),
                        response.get().codeDeliveryDetails().deliveryMediumAsString(),
                        response.get().userSub());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to sign up user, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }

    public SignInResponseDto signInUser(SignInUserDto signInUserDto) {
        try {

            Map<String, String> authParams = new LinkedHashMap<>() {{
                put("USERNAME", signInUserDto.email());
                put("PASSWORD", signInUserDto.password());
            }};

            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .clientId(CLIENT_ID)
                    .userPoolId(USERPOOL_ID)
                    .authParameters(authParams)
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .build();

            Optional<AuthenticationResultType> response = Optional.ofNullable(cognitoIdentityProviderClient.adminInitiateAuth(authRequest).authenticationResult());

            if (response.isPresent()) {
                log.info("User {} signed in", signInUserDto.email());
                return new SignInResponseDto(response.get().accessToken(),
                        response.get().tokenType(),
                        response.get().refreshToken(),
                        response.get().idToken(),
                        response.get().expiresIn());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to sign in user, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }

    public ConfirmSignUpResponseDto confirmSignUp(ConfirmSignUpDto confirmSignUpDto) {
        try {
            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(CLIENT_ID)
                    .confirmationCode(confirmSignUpDto.code())
                    .username(confirmSignUpDto.email())
                    .build();

            log.info("User {} sign up confirmed", confirmSignUpDto.email());

            Optional<ConfirmSignUpResponse> response = Optional.ofNullable(cognitoIdentityProviderClient.confirmSignUp(signUpRequest));
            if (response.isPresent()) {
                return new ConfirmSignUpResponseDto("CONFIRMED");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to sign up user, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }

    public ChangePasswordResponseDto changePassword(ChangePasswordDto changePasswordDto) {
        try {
            ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                    .accessToken(changePasswordDto.accessToken())
                    .previousPassword(changePasswordDto.oldPassword())
                    .proposedPassword(changePasswordDto.newPassword())
                    .build();

            log.info("User password changed");
            return new ChangePasswordResponseDto(Optional.ofNullable(cognitoIdentityProviderClient.changePassword(changePasswordRequest)), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to change user password, error: {}", e.awsErrorDetails().errorMessage());
            return new ChangePasswordResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        try {
            ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                    .clientId(CLIENT_ID)
                    .username(forgotPasswordDto.email())
                    .build();

            log.info("User {} password restart process started", forgotPasswordDto.email());
            return new ForgotPasswordResponseDto(Optional.ofNullable(cognitoIdentityProviderClient.forgotPassword(forgotPasswordRequest)), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to start forgot password process, error: {}", e.awsErrorDetails().errorMessage());
            return new ForgotPasswordResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public ConfirmForgotPasswordResponseDto confirmForgotPassword(ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        try {
            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                    .clientId(CLIENT_ID)
                    .username(confirmForgotPasswordDto.email())
                    .confirmationCode(confirmForgotPasswordDto.confirmationCode())
                    .password(confirmForgotPasswordDto.password())
                    .build();

            log.info("User {} password reseted successfully", confirmForgotPasswordDto.email());
            return new ConfirmForgotPasswordResponseDto(Optional.of(cognitoIdentityProviderClient.confirmForgotPassword(confirmForgotPasswordRequest)), null);
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to reset user password, error: {}", e.awsErrorDetails().errorMessage());
            return new ConfirmForgotPasswordResponseDto(Optional.empty(), e.awsErrorDetails().errorMessage());
        }
    }


}
