package com.user_service.services;


import com.user_service.DTOs.LoginUserDto;
import com.user_service.DTOs.RegisterUserDto;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UserAuthenticationService {

    private CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
            .region(Region.EU_NORTH_1)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    private CognitoIdentityClient cognitoClient = CognitoIdentityClient.builder()
            .region(Region.EU_NORTH_1)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    public void printUserDetails(RegisterUserDto registerUserDto) {
        signUp(identityProviderClient, "temp", registerUserDto);
    }

    public void printUserDetails(LoginUserDto loginUserDto) {
        signIn(cognitoClient, "temp", "temp", loginUserDto);
        cognitoClient.close();
    }

    private void signUp(CognitoIdentityProviderClient cognitoIdentityProviderClient, String clientId, RegisterUserDto registerUserDto) {

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
                    .clientId(clientId)
                    .password(registerUserDto.getPassword())
                    .build();

            cognitoIdentityProviderClient.signUp(signUpRequest);
            System.out.println("User has been signed up");

        } catch(CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    private void signIn (CognitoIdentityClient cognitoIdentityClient, String clientId, String userPoolId, LoginUserDto loginUserDto) {
        try {
        Map<String, String> authParams = new LinkedHashMap<String, String>() {{
            put("USERNAME", loginUserDto.getEmail());
            put("PASSWORD", loginUserDto.getPassword());
        }};

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .clientId(clientId)
                .userPoolId(userPoolId)
                .authParameters(authParams)
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .build();

        AdminInitiateAuthResponse response = identityProviderClient.adminInitiateAuth(authRequest);
        System.out.println("Result Challenge is : " + response.toString() );

    } catch(CognitoIdentityProviderException e) {
        System.err.println(e.awsErrorDetails().errorMessage());
        System.exit(1);
    }
    }
}
