package com.user_service.services


import com.user_service.controllers.UserAuthorizationController
import com.user_service.dtos.request.AuthorizeDto
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
        given: "AuthorizeDto is provided"
            AuthorizeDto authorizeDto =
                    new AuthorizeDto("123")

        and: "AuthorizeResponseDto is provided"
            AuthorizeResponseDto authorizeResponseDto =
                    new AuthorizeResponseDto(true)

        and: "authorize method is mocked"
            userAuthorizationService.authorize(authorizeDto) >> authorizeResponseDto

        when: "authorize method is called"
            def authorizeResponse = userAuthorizationController.authorize(authorizeDto)

        then: "expected values are returned"
            authorizeResponse.getStatusCode() == HttpStatus.OK
            authorizeResponse.getBody().authorized() == true
    }
}
