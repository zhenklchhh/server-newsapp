package com.logic.server_newsapp.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class AuthorizationVerifier {
    private static final JWSVerifier jwsVerifier = new MACVerifier(KeyManager.decodeToBytes(SecretKey.SECRET_KEY));
    public static boolean verify(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SignedJWT jwtToken = SignedJWT.parse(token);
                if (!jwtToken.verify(jwsVerifier)) {
                    return false;
                }
                JWTClaimsSet claimsSet = (JWTClaimsSet) jwtToken.getJWTClaimsSet();
                Date expirationTime = claimsSet.getExpirationTime();

                if (expirationTime == null || expirationTime.before(new Date())) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Invalid JWT token");
            }
        } else {
            throw new RuntimeException("Missing Authorization header");
        }
    }
}
