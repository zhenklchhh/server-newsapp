package com.logic.server_newsapp.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JWTGenerator {

    public static String signJWT(long userId) throws IOException, JOSEException {
        byte[] secret = KeyManager.decodeToBytes(SecretKey.SECRET_KEY);
        JWSSigner signer = new MACSigner(secret);

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject(String.valueOf(userId));
        claimsSet.setIssuer("http://localhost:8080");
        claimsSet.setExpirationTime(new Date(new Date().getTime() + 60 * 60 * 1000));
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}