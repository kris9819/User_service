package com.user_service.configuration;

import com.user_service.services.UserAuthenticationService;
import com.user_service.services.UserAuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AppConfig {

    @Bean
    public UserAuthenticationService userAuthenticationServiceBean(CognitoIdentityProviderClient cognitoIdentityProviderClient) {
        return new UserAuthenticationService(cognitoIdentityProviderClient);
    }

    @Bean
    public UserAuthorizationService userAuthorizationServiceBean() {
        return new UserAuthorizationService();
    }

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}


