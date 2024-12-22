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

@SuppressWarnings("checkstyle:FinalClass")
@Component
public final class JWTGenerator {
    private JWTGenerator() {
    }

    /**
     * Signs a JWT with the given user ID.
     *
     * @param userId The ID of the user for whom the JWT is generated.
     * @return The serialized JWT token.
     * @throws IOException If an I/O error occurs.
     * @throws JOSEException If a JOSE error occurs.
     */
    public static String signJWT(final long userId)
            throws IOException, JOSEException {
        byte[] secret = KeyManager.decodeToBytes(SecretKey.SECRET_KEY);
        JWSSigner signer = new MACSigner(secret);

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject(String.valueOf(userId));
        claimsSet.setIssuer("http://localhost:8080");
        final int seconds = 60;
        final int thousands = 1000;
        claimsSet.setExpirationTime(
                new Date(new Date().getTime() + seconds * seconds * thousands));
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}
