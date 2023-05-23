package com.user_service.configuration;

import com.user_service.services.UserAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserAuthenticationService userAuthenticationServiceBean() {
        return new UserAuthenticationService();
    }
}
