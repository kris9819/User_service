package com.user_service.services;


import com.user_service.DTOs.*;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class UserAuthenticationService {

    private CognitoIdentityClient cognitoIdentityClient;

    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public static final String CLIENT_ID = "1j3sjiopi69flk3nt6g58cnspa";
    public static final String USERPOOL_ID = "eu-north-1_pua0XpF66";

    public SignUpResponseDto signUpUser(RegisterUserDto registerUserDto) {
        AttributeType attributeType = AttributeType.builder()
                .name("email")
                .value(registerUserDto.getEmail())
                .build();

        AttributeType attributeType1 = AttributeType.builder()
                .name("name")
                .value(registerUserDto.getName())
                .build();

        AttributeType attributeType2 = AttributeType.builder()
                .name("birthdate")
                .value(registerUserDto.getBirthdate().toString())
                .build();

        List<AttributeType> attrs = new ArrayList<>();
        attrs.add(attributeType);
        attrs.add(attributeType1);
        attrs.add(attributeType2);
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(registerUserDto.getEmail())
                    .clientId(CLIENT_ID)
                    .password(registerUserDto.getPassword())
                    .build();

            return new SignUpResponseDto(cognitoIdentityProviderClient.signUp(signUpRequest), null);

        } catch(CognitoIdentityProviderException e) {
            return new SignUpResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }

    public void signInUser(LoginUserDto loginUserDto) {
        try {
            Map<String, String> authParams = new LinkedHashMap<String, String>() {{
                put("USERNAME", loginUserDto.getEmail());
                put("PASSWORD", loginUserDto.getPassword());
            }};

            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .clientId(CLIENT_ID)
                    .userPoolId(USERPOOL_ID)
                    .authParameters(authParams)
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .build();

            AdminInitiateAuthResponse response = cognitoIdentityProviderClient.adminInitiateAuth(authRequest);
            System.out.println("Result Challenge is : " + response.toString() );

        } catch(CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        cognitoIdentityClient.close();
    }

    public ConfirmSignUpResponseDto confirmSignUp(ConfirmRegisterDto confirmRegisterDto) {
        try {
            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(CLIENT_ID)
                    .confirmationCode(confirmRegisterDto.getCode())
                    .username(confirmRegisterDto.getEmail())
                    .build();

            return new ConfirmSignUpResponseDto(cognitoIdentityProviderClient.confirmSignUp(signUpRequest), confirmRegisterDto.getEmail() +" was confirmed");

        } catch(CognitoIdentityProviderException e) {
            return new ConfirmSignUpResponseDto(null, e.awsErrorDetails().errorMessage());
        }
    }
}
