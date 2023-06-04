package com.user_service.services


import com.user_service.utility.SSLCertificationHandling
import org.json.JSONObject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserAuthenticationServiceTest extends Specification {

    RestTemplate restTemplate
    HttpHeaders headers = new HttpHeaders()

    def "Should register new user"() {
        given:
            SSLCertificationHandling.ignoreCertificates()
            restTemplate = new RestTemplate()
            JSONObject registerUserObject = new JSONObject()
            def resourceUrl = "https://localhost:8081/register"
            headers.setContentType(MediaType.APPLICATION_JSON)

            registerUserObject.put("name", "jaroslaw")
            registerUserObject.put("email", "prosze.dzialajjjjj@onet.pl")
            registerUserObject.put("birthdate", "12-12-1999")
            registerUserObject.put("password", "dfG123!@0123456")
            registerUserObject.put("passwordRepeat", "dfG123!@0123456")
            HttpEntity<String> entity = new HttpEntity<String>(registerUserObject.toString(), headers)

        when:
            ResponseEntity<String> registerResponse = restTemplate
                    .exchange(resourceUrl, HttpMethod.POST, entity, String.class)

        then:
            registerResponse.getStatusCode() == HttpStatus.OK
            registerResponse.getBody() == "Account sign up, confirmation code sent to prosze.dzialajjjjj@onet.pl"
    }

//    def "Should handle errors on register"() {
//        given:
//            SSLCertificationHandling.ignoreCertificates()
//            restTemplate = new RestTemplate()
//            JSONObject registerUserObject = new JSONObject()
//            def resourceUrl = "https://localhost:8081/register"
//            headers.setContentType(MediaType.APPLICATION_JSON)
//
//            registerUserObject.put("name", "jaroslaw")
//            registerUserObject.put("email", "prosze.dzialajjjjj@onet.pl")
//            registerUserObject.put("birthdate", "12-12-1999")
//            registerUserObject.put("password", "dfG123!@0123456")
//            registerUserObject.put("passwordRepeat", "dfG123!@0123456")
//            HttpEntity<String> entity = new HttpEntity<String>(registerUserObject.toString(), headers)
//
//        when:
//            ResponseEntity<String> registerResponse = restTemplate
//                    .exchange(resourceUrl, HttpMethod.POST, entity, String.class)
//
//        then:
//            registerResponse.getStatusCode() == HttpStatus.ACCEPTED
//            registerResponse.getBody() != null
//    }

    def "Should login user"() {
        given:
        SSLCertificationHandling.ignoreCertificates()
        restTemplate = new RestTemplate()
        JSONObject logingUserObject = new JSONObject()
        def resourceUrl = "https://localhost:8081/login"
        headers.setContentType(MediaType.APPLICATION_JSON)

        logingUserObject.put("email", "prosze.dzialajjjjj@onet.pl")
        logingUserObject.put("password", "dfG123!@0123456")
        HttpEntity<String> entity = new HttpEntity<String>(logingUserObject.toString(), headers)

        when:
        ResponseEntity<String> registerResponse = restTemplate
                .exchange(resourceUrl, HttpMethod.POST, entity, String.class)

        then:
        registerResponse.getStatusCode() == HttpStatus.OK
        registerResponse.getBody() == "Login successful"
    }

    def "Should change password"() {
        given:
        SSLCertificationHandling.ignoreCertificates()
        restTemplate = new RestTemplate()
        JSONObject changePasswordObject = new JSONObject()
        def resourceUrl = "https://localhost:8081/changePassword"
        headers.setContentType(MediaType.APPLICATION_JSON)

        changePasswordObject.put("accessToken", "")
        changePasswordObject.put("oldPassword", "dfG123!@0123456")
        changePasswordObject.put("newPassword", "dfG123!@0123456789")
        HttpEntity<String> entity = new HttpEntity<String>(changePasswordObject.toString(), headers)

        when:
        ResponseEntity<String> registerResponse = restTemplate
                .exchange(resourceUrl, HttpMethod.POST, entity, String.class)

        then:
        registerResponse.getStatusCode() == HttpStatus.OK
        registerResponse.getBody() == "Password changed"
    }

    def "Should send verification code when forgot password"() {
        given:
        SSLCertificationHandling.ignoreCertificates()
        restTemplate = new RestTemplate()
        JSONObject forgotPasswordObject = new JSONObject()
        def resourceUrl = "https://localhost:8081/forgotPassword"
        headers.setContentType(MediaType.APPLICATION_JSON)

        forgotPasswordObject.put("email", "prosze.dzialajjjjj@onet.pl")
        HttpEntity<String> entity = new HttpEntity<String>(forgotPasswordObject.toString(), headers)

        when:
        ResponseEntity<String> registerResponse = restTemplate
                .exchange(resourceUrl, HttpMethod.POST, entity, String.class)

        then:
        registerResponse.getStatusCode() == HttpStatus.OK
        registerResponse.getBody() == "Forgot password validation code send on email"
    }

//    def "Should reset password"() {
//        given:
//        SSLCertificationHandling.ignoreCertificates()
//        restTemplate = new RestTemplate()
//        JSONObject forgotPasswordObject = new JSONObject()
//        def resourceUrl = "https://localhost:8081/confirmForgotPassword"
//        headers.setContentType(MediaType.APPLICATION_JSON)
//
//        forgotPasswordObject.put("email", "prosze.dzialajjjjj@onet.pl")
//        forgotPasswordObject.put("confirmationCode", "prosze.dzialajjjjj@onet.pl")
//        forgotPasswordObject.put("password", "prosze.dzialajjjjj@onet.pl")
//        HttpEntity<String> entity = new HttpEntity<String>(forgotPasswordObject.toString(), headers)
//
//        when:
//        ResponseEntity<String> registerResponse = restTemplate
//                .exchange(resourceUrl, HttpMethod.POST, entity, String.class)
//
//        then:
//        registerResponse.getStatusCode() == HttpStatus.OK
//        registerResponse.getBody() == "Forgot password validation code send on email"
//    }

}
