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
import com.user_service.utility.ExternalCognitoClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserAuthenticationService {

    private ExternalCognitoClient externalCognitoClient;

    public SignUpResponseDto signUpUser(SignUpUserDto signUpUserDto) {
        try {
            Optional<SignUpResponse> response = externalCognitoClient.signUpUser(signUpUserDto);

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
            Optional<AuthenticationResultType> response = externalCognitoClient.signInUser(signInUserDto);

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
            Optional<ConfirmSignUpResponse> response = externalCognitoClient.confirmSignUpUser(confirmSignUpDto);
            if (response.isPresent()) {
                log.info("User {} sign up confirmed", confirmSignUpDto.email());
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
            Optional<ChangePasswordResponse> response = externalCognitoClient.changeUserPassword(changePasswordDto);
            if (response.isPresent()) {
                log.info("User password changed");
                return new ChangePasswordResponseDto("CHANGED");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to change user password, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }

    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        try {
            Optional<ForgotPasswordResponse> response = externalCognitoClient.userForgotPassword(forgotPasswordDto);
            if (response.isPresent()) {
                log.info("User {} password restart process started", forgotPasswordDto.email());
                return new ForgotPasswordResponseDto(response.get().codeDeliveryDetails().destination(), response.get().codeDeliveryDetails().deliveryMediumAsString());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to start forgot password process, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }

    public ConfirmForgotPasswordResponseDto confirmForgotPassword(ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        try {
            Optional<ConfirmForgotPasswordResponse> response = externalCognitoClient.userConfirmForgotPassword(confirmForgotPasswordDto);
            if (response.isPresent()) {
                log.info("User {} password reseted successfully", confirmForgotPasswordDto.email());
                return new ConfirmForgotPasswordResponseDto("SUCCESS");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        } catch (CognitoIdentityProviderException e) {
            log.error("Failed to reset user password, error: {}", e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.awsErrorDetails().errorMessage());
        }
    }


}
