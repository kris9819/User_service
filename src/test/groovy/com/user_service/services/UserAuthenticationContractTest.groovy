package com.user_service.services

import com.user_service.controllers.UserAuthenticationController
import com.user_service.dtos.request.*
import com.user_service.dtos.response.*
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDate

class UserAuthenticationContractTest extends Specification {

    UserAuthenticationController userAuthenticationController
    UserAuthenticationService userAuthenticationService

    def setup() {
        userAuthenticationService = Mock(UserAuthenticationService)
        userAuthenticationController = new UserAuthenticationController(userAuthenticationService)
    }

    def "Should register new user"() {
        given: "SignUpUserDto is provided"
            SignUpUserDto signUpUserDto =
                    new SignUpUserDto("jarek", "jarek.jarkowski@gmail.com", LocalDate.of(1993, 7, 24), "dfG123!@0123456", "dfG123!@0123456")

        and: "SignUpResponseDto is provided"
            SignUpResponseDto signUpResponseDto =
                    new SignUpResponseDto("j***@g***.com", "email", "1234")

        and: "signUp method is mocked"
            userAuthenticationService.signUpUser(signUpUserDto) >> signUpResponseDto

        when: "signUp method is called"
            def registerResponse = userAuthenticationController.signUpUser(signUpUserDto)

        then: "expected values are returned"
            registerResponse.getStatusCode() == HttpStatus.OK
            registerResponse.getBody().email() == "j***@g***.com"
            registerResponse.getBody().deliveryMedium() == "email"
            registerResponse.getBody().userSub() == "1234"
    }

    def "Should login user"() {
        given: "SignIpUserDto is provided"
            SignInUserDto signInUserDto =
                    new SignInUserDto("jarek.jarkowski@gmail.com", "dfG123!@0123456")

        and: "SignInResponseDto is provided"
            SignInResponseDto signInResponseDto =
                    new SignInResponseDto("123", "bearer", "456", "789", 3600)

        and: "signIn method is mocked"
            userAuthenticationService.signInUser(signInUserDto) >> signInResponseDto

        when: "signIn method is called"
            def loginResponse = userAuthenticationController.signInUser(signInUserDto)

        then: "expected values are returned"
            loginResponse.getStatusCode() == HttpStatus.OK
            loginResponse.getBody().accessToken() == "123"
            loginResponse.getBody().tokenType() == "bearer"
            loginResponse.getBody().refreshToken() == "456"
            loginResponse.getBody().idToken() == "789"
            loginResponse.getBody().expiresIn() == 3600
    }

    def "Should change user password"() {
        given: "ChangePasswordDto is provided"
            ChangePasswordDto changePasswordDto =
                    new ChangePasswordDto("123", "dfG123!@0123456", "dfG123!@0123456789")

        and: "ChangePasswordResponseDto is provided"
            ChangePasswordResponseDto changePasswordResponseDto =
                    new ChangePasswordResponseDto("CHANGED")

        and: "changePassword method is mocked"
            userAuthenticationService.changePassword(changePasswordDto) >> changePasswordResponseDto

        when: "changePassword method is called"
            def changePasswordResponse = userAuthenticationController.changePassword(changePasswordDto)

        then: "expected values are returned"
            changePasswordResponse.getStatusCode() == HttpStatus.OK
            changePasswordResponse.getBody().message() == "CHANGED"
    }

    def "Should send verification code when forgot password"() {
        given: "ForgotPasswordDto is provided"
            ForgotPasswordDto forgotPasswordDto =
                    new ForgotPasswordDto("jarek@gmail.com")

        and: "ForgotPasswordResponseDto is provided"
            ForgotPasswordResponseDto forgotPasswordResponseDto =
                    new ForgotPasswordResponseDto("j***@g***.com", "email")

        and: "forgotPassword method is mocked"
            userAuthenticationService.forgotPassword(forgotPasswordDto) >> forgotPasswordResponseDto

        when: "forgotPassword method is called"
            def forgotPasswordResponse = userAuthenticationController.forgotPassword(forgotPasswordDto)

        then: "expected values are returned"
            forgotPasswordResponse.getStatusCode() == HttpStatus.OK
            forgotPasswordResponse.getBody().email() == "j***@g***.com"
            forgotPasswordResponse.getBody().deliveryMedium() == "email"
    }

    def "Should change password after providing code"() {
        given: "ConfirmForgotPasswordDto is provided"
            ConfirmForgotPasswordDto confirmForgotPasswordDto =
                    new ConfirmForgotPasswordDto("jarek@gmail.com", "123", "dfG123!@0123456789")

        and: "ConfirmForgotPasswordResponseDto is provided"
            ConfirmForgotPasswordResponseDto confirmForgotPasswordResponseDto =
                    new ConfirmForgotPasswordResponseDto("SUCCESS")

        and: "confirmForgotPassword method is mocked"
            userAuthenticationService.confirmForgotPassword(confirmForgotPasswordDto) >> confirmForgotPasswordResponseDto

        when: "confirmForgotPassword method is called"
            def confirmForgotPasswordResponse = userAuthenticationController.confirmForgotPassword(confirmForgotPasswordDto)

        then: "expected values are returned"
            confirmForgotPasswordResponse.getStatusCode() == HttpStatus.OK
            confirmForgotPasswordResponse.getBody().message() == "SUCCESS"
    }

    def "Should confirm registration after providing code"() {
        given: "ConfirmSignUpDto is provided"
            ConfirmSignUpDto confirmSignUpDto =
                    new ConfirmSignUpDto("123", "jarek@gmail.com")

        and: "ConfirmForgotPasswordResponseDto is provided"
            ConfirmSignUpResponseDto confirmSignUpResponseDto =
                    new ConfirmSignUpResponseDto("CONFIRMED")

        and: "confirmSignUp method is mocked"
            userAuthenticationService.confirmSignUp(confirmSignUpDto) >> confirmSignUpResponseDto

        when: "confirmSignUp method is called"
            def confirmSignUp = userAuthenticationController.confirmSignUp(confirmSignUpDto)

        then: "expected values are returned"
            confirmSignUp.getStatusCode() == HttpStatus.OK
            confirmSignUp.getBody().message() == "CONFIRMED"
    }

}
