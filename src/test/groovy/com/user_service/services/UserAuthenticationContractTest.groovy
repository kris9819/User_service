package com.user_service.services

import com.user_service.controllers.UserAuthenticationController
import com.user_service.dtos.response.*
import com.user_service.utility.ExternalCognitoClient
import com.user_service.utility.SSLCertificationHandling
import org.json.JSONObject
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.*
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserAuthenticationContractTest extends Specification {

    RestTemplate restTemplate = new RestTemplate()
    HttpHeaders headers = new HttpHeaders()

    
    UserAuthenticationController userAuthenticationController
    
    UserAuthenticationService userAuthenticationService
    
    ExternalCognitoClient externalCognitoClient
    
    @SpringBean
    CognitoIdentityProviderClient cognitoIdentityProviderClient = Mock(CognitoIdentityProviderClient)

    def setup() {
        externalCognitoClient = new ExternalCognitoClient(cognitoIdentityProviderClient)
        userAuthenticationService = new UserAuthenticationService(externalCognitoClient)
        userAuthenticationController = new UserAuthenticationController(userAuthenticationService)
        SSLCertificationHandling.ignoreCertificates()
        headers.setContentType(MediaType.APPLICATION_JSON)
    }

    def "Should register new user"() {
        given: "SignUpUserDto is provided"
            JSONObject registerUserObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/register"

            registerUserObject.put("name", "jaroslaw")
            registerUserObject.put("email", "jaroslaw@gmail.com")
            registerUserObject.put("birthdate", "12-12-1999")
            registerUserObject.put("password", "dfG123!@0123456")
            registerUserObject.put("passwordRepeat", "dfG123!@0123456")
            HttpEntity<String> entity = new HttpEntity<String>(registerUserObject.toString(), headers)

        and: "SignUpResponseDto is provided"
            SignUpResponse signUpResponse = SignUpResponse.builder()
                .userSub("1234")
                .codeDeliveryDetails(CodeDeliveryDetailsType.builder()
                                        .deliveryMedium("email")
                                        .destination("j***@g***.com")
                                        .build())
                .build()


        when: "signUp method is called"
            ResponseEntity<SignUpResponseDto> registerResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, SignUpResponseDto.class)


        then: "signUp method is mocked"
            cognitoIdentityProviderClient.signUp(_) >> signUpResponse

        and: "expected values are returned"
            registerResponse.getStatusCode() == HttpStatus.OK
            registerResponse.getBody().email() == "j***@g***.com"
            registerResponse.getBody().deliveryMedium() == "email"
            registerResponse.getBody().userSub() == "1234"
    }

    def "Should login user"() {
        given: "SignIpUserDto is provided"
            JSONObject logingUserObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/login"

            logingUserObject.put("email", "prosze.dzialajjjjj@onet.pl")
            logingUserObject.put("password", "dfG123!@0123456")
            HttpEntity<String> entity = new HttpEntity<String>(logingUserObject.toString(), headers)

        and: "SignInResponseDto is provided"
            AdminInitiateAuthResponse adminInitiateAuthResponse = AdminInitiateAuthResponse.builder()
                .authenticationResult(AuthenticationResultType.builder()
                                    .accessToken("123")
                                    .tokenType("bearer")
                                    .refreshToken("456")
                                    .idToken("789")
                                    .expiresIn(3600)
                                    .build())

                .build()

        and: "signIn method is mocked"
            cognitoIdentityProviderClient.adminInitiateAuth(_) >> adminInitiateAuthResponse

        when: "signIn method is called"
            ResponseEntity<SignInResponseDto> loginResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, SignInResponseDto.class)

        then: "expected values are returned"
            loginResponse.getStatusCode() == HttpStatus.OK
            loginResponse.getBody().accessToken() == "123"
            loginResponse.getBody().tokenType() == "bearer"
            loginResponse.getBody().refreshToken() == "456"
            loginResponse.getBody().idToken() == "789"
            loginResponse.getBody().expiresInMs() == 3600
    }

    def "Should change user password"() {
        given: "ChangePasswordDto is provided"
            JSONObject changePasswordObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/changePassword"

            changePasswordObject.put("accessToken", "123")
            changePasswordObject.put("oldPassword", "dfG123!@0123456")
            changePasswordObject.put("newPassword", "dfG123!@0123456789")
            HttpEntity<String> entity = new HttpEntity<String>(changePasswordObject.toString(), headers)

        and: "ChangePasswordResponseDto is provided"
        ChangePasswordResponse changePasswordResponse = ChangePasswordResponse.builder().build()

        and: "changePassword method is mocked"
            cognitoIdentityProviderClient.changePassword(_) >> changePasswordResponse

        when: "changePassword method is called"
            ResponseEntity<ChangePasswordResponseDto> changPasswordResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, ChangePasswordResponseDto.class)

        then: "expected values are returned"
            changPasswordResponse.getStatusCode() == HttpStatus.OK
            changPasswordResponse.getBody().message() == "CHANGED"
    }

    def "Should send verification code when forgot password"() {
        given: "ForgotPasswordDto is provided"
            JSONObject forgotPasswordObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/forgotPassword"

            forgotPasswordObject.put("email", "jaroslaw@gmail.com")
            HttpEntity<String> entity = new HttpEntity<String>(forgotPasswordObject.toString(), headers)

        and: "ForgotPasswordResponseDto is provided"
            ForgotPasswordResponse forgotPasswordResponseCognito = ForgotPasswordResponse.builder()
                .codeDeliveryDetails(CodeDeliveryDetailsType.builder()
                                        .destination("j***@g***.com")
                                        .deliveryMedium("email")
                                        .build())
                .build()

        and: "forgotPassword method is mocked"
            cognitoIdentityProviderClient.forgotPassword(_) >> forgotPasswordResponseCognito

        when: "forgotPassword method is called"
            ResponseEntity<ForgotPasswordResponseDto> forgotPasswordResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, ForgotPasswordResponseDto.class)

        then: "expected values are returned"
            forgotPasswordResponse.getStatusCode() == HttpStatus.OK
            forgotPasswordResponse.getBody().email() == "j***@g***.com"
            forgotPasswordResponse.getBody().deliveryMedium() == "email"
    }

    def "Should change password after providing code"() {
        given: "ConfirmForgotPasswordDto is provided"
            JSONObject forgotPasswordObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/confirmForgotPassword"

            forgotPasswordObject.put("email", "jaroslaw@gmail.com")
            forgotPasswordObject.put("confirmationCode", "123")
            forgotPasswordObject.put("password", "dfG123!@0123456789")
            HttpEntity<String> entity = new HttpEntity<String>(forgotPasswordObject.toString(), headers)

        and: "ConfirmForgotPasswordResponseDto is provided"
            ConfirmForgotPasswordResponse confirmForgotPasswordResponseCognito = ConfirmForgotPasswordResponse.builder().build()

        and: "confirmForgotPassword method is mocked"
            cognitoIdentityProviderClient.confirmForgotPassword(_) >> confirmForgotPasswordResponseCognito

        when: "confirmForgotPassword method is called"
            ResponseEntity<ConfirmForgotPasswordResponseDto> confirmForgotPasswordResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, ConfirmForgotPasswordResponseDto.class)
        then: "expected values are returned"
            confirmForgotPasswordResponse.getStatusCode() == HttpStatus.OK
            confirmForgotPasswordResponse.getBody().message() == "SUCCESS"
    }

    def "Should confirm registration after providing code"() {
        given: "ConfirmSignUpDto is provided"
            JSONObject confirmSignUpObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/confirm"

            confirmSignUpObject.put("email", "jaroslaw@gmail.com")
            confirmSignUpObject.put("code", "123")
            HttpEntity<String> entity = new HttpEntity<String>(confirmSignUpObject.toString(), headers)

        and: "ConfirmForgotPasswordResponseDto is provided"
            ConfirmSignUpResponse confirmSignUpResponseCognito = ConfirmSignUpResponse.builder().build()

        and: "confirmSignUp method is mocked"
            cognitoIdentityProviderClient.confirmSignUp(_) >> confirmSignUpResponseCognito

        when: "confirmSignUp method is called"
            ResponseEntity<ConfirmSignUpResponseDto> confirmSignUpResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, ConfirmSignUpResponseDto.class)
        then: "expected values are returned"
            confirmSignUpResponse.getStatusCode() == HttpStatus.OK
            confirmSignUpResponse.getBody().message() == "CONFIRMED"
    }

}
