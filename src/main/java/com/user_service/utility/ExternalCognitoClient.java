package com.user_service.utility;

import com.user_service.dtos.request.ChangePasswordDto;
import com.user_service.dtos.request.ConfirmForgotPasswordDto;
import com.user_service.dtos.request.ConfirmSignUpDto;
import com.user_service.dtos.request.ForgotPasswordDto;
import com.user_service.dtos.request.SignInUserDto;
import com.user_service.dtos.request.SignUpUserDto;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class ExternalCognitoClient {

    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public static final String CLIENT_ID = "1j3sjiopi69flk3nt6g58cnspa";
    public static final String USERPOOL_ID = "eu-north-1_pua0XpF66";

    public Optional<SignUpResponse> signUpUser(SignUpUserDto signUpUserDto) {
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

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userAttributes(attrs)
                .username(signUpUserDto.email())
                .clientId(CLIENT_ID)
                .password(signUpUserDto.password())
                .build();

        return Optional.ofNullable(cognitoIdentityProviderClient.signUp(signUpRequest));
    }

    public Optional<AuthenticationResultType> signInUser(SignInUserDto signInUserDto) {

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

        return Optional.ofNullable(cognitoIdentityProviderClient.adminInitiateAuth(authRequest).authenticationResult());
    }

    public Optional<ConfirmSignUpResponse> confirmSignUpUser(ConfirmSignUpDto confirmSignUpDto) {
        ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                .clientId(CLIENT_ID)
                .confirmationCode(confirmSignUpDto.code())
                .username(confirmSignUpDto.email())
                .build();

        return Optional.ofNullable(cognitoIdentityProviderClient.confirmSignUp(signUpRequest));
    }

    public Optional<ChangePasswordResponse> changeUserPassword(ChangePasswordDto changePasswordDto) {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .accessToken(changePasswordDto.accessToken())
                .previousPassword(changePasswordDto.oldPassword())
                .proposedPassword(changePasswordDto.newPassword())
                .build();


        return Optional.ofNullable(cognitoIdentityProviderClient.changePassword(changePasswordRequest));
    }

    public Optional<ForgotPasswordResponse> userForgotPassword (ForgotPasswordDto forgotPasswordDto) {
        ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                .clientId(CLIENT_ID)
                .username(forgotPasswordDto.email())
                .build();

        return Optional.ofNullable(cognitoIdentityProviderClient.forgotPassword(forgotPasswordRequest));
    }

    public Optional<ConfirmForgotPasswordResponse> userConfirmForgotPassword(ConfirmForgotPasswordDto confirmForgotPasswordDto) {
        ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                .clientId(CLIENT_ID)
                .username(confirmForgotPasswordDto.email())
                .confirmationCode(confirmForgotPasswordDto.confirmationCode())
                .password(confirmForgotPasswordDto.password())
                .build();

        return Optional.ofNullable(cognitoIdentityProviderClient.confirmForgotPassword(confirmForgotPasswordRequest));
    }

    public Optional<GetUserResponse> getUserInfo (String token) {
        GetUserRequest getUserRequest = GetUserRequest.builder()
                .accessToken(token)
                .build();

        return Optional.ofNullable(cognitoIdentityProviderClient.getUser(getUserRequest));
    }
}
