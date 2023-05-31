package com.user_service.configuration;

import com.user_service.services.UserAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AppConfig {

    @Bean
    public UserAuthenticationService userAuthenticationServiceBean(CognitoIdentityProviderClient cognitoIdentityProviderClient, CognitoIdentityClient cognitoIdentityClient) {
        return new UserAuthenticationService(cognitoIdentityClient, cognitoIdentityProviderClient);
    }

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    @Bean
    public CognitoIdentityClient cognitoIdentityClient() {
        return CognitoIdentityClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}


