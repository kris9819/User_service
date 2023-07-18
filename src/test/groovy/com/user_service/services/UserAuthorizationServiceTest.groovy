package com.user_service.services

import com.user_service.controllers.UserAuthorizationController
import com.user_service.dtos.response.AuthorizeResponseDto
import org.springframework.http.HttpStatus
import spock.lang.Specification

class UserAuthorizationServiceTest extends Specification {

    UserAuthorizationController userAuthorizationController
    UserAuthorizationService userAuthorizationService

    def setup() {
        userAuthorizationService = Mock(UserAuthorizationService)
        userAuthorizationController = new UserAuthorizationController(userAuthorizationService)
    }

    def "Should authorize user request"() {
        given: "AccessToken is provided"
            String accessToken = "123"

        and: "AuthorizeResponseDto is provided"
            AuthorizeResponseDto authorizeResponseDto =
                    new AuthorizeResponseDto(true)

        and: "authorize method is mocked"
            userAuthorizationService.authorize(accessToken) >> authorizeResponseDto

        when: "authorize method is called"
            def authorizeResponse = userAuthorizationController.authorize(authorizeDto)

        then: "expected values are returned"
            authorizeResponse.getStatusCode() == HttpStatus.OK
            authorizeResponse.getBody().authorized() == true
    }
}
