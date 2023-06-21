package com.user_service.configuration;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.user_service.services.UserAuthenticationService;
import com.user_service.services.UserAuthorizationService;
import com.user_service.utility.AwsCognitoRSAKeyProvider;
import com.user_service.utility.ExternalCognitoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AppConfig {

    @Bean
    public UserAuthenticationService userAuthenticationServiceBean(ExternalCognitoClient externalCognitoClient) {
        return new UserAuthenticationService(externalCognitoClient);
    }

    @Bean
    public UserAuthorizationService userAuthorizationServiceBean(RSAKeyProvider rsaKeyProvider, ExternalCognitoClient externalCognitoClient) {
        return new UserAuthorizationService(rsaKeyProvider, externalCognitoClient);
    }

    @Bean
    public RSAKeyProvider rsaKeyProvider() {
        return new AwsCognitoRSAKeyProvider();
    }

    @Bean ExternalCognitoClient externalCognitoClient(CognitoIdentityProviderClient cognitoIdentityProviderClient) {
        return new ExternalCognitoClient(cognitoIdentityProviderClient);
    }

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}


