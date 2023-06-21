package com.user_service.utility;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class AwsCognitoRSAKeyProvider implements RSAKeyProvider {

    private static final String AWS_KID_STORE= "https://cognito-idp.eu-north-1.amazonaws.com/eu-north-1_pua0XpF66/.well-known/jwks.json";

    private final JwkProvider provider;

    public AwsCognitoRSAKeyProvider() {
        URL awsKidStoreUrl;
        try {
            awsKidStoreUrl = new URL(AWS_KID_STORE);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invialid URL provided, URL=" + AWS_KID_STORE);
        }
        provider = new JwkProviderBuilder(awsKidStoreUrl).build();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try {
            return (RSAPublicKey) provider.get(kid).getPublicKey();
        } catch (JwkException e) {
            throw new RuntimeException("Failed to get JWT kid");
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
