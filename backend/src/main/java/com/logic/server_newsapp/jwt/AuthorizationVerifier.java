package com.logic.server_newsapp.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;

/** Utility class for verifying JWTs. */
@SuppressWarnings("checkstyle:FinalClass")
public final class AuthorizationVerifier {

    private AuthorizationVerifier() {
    }

    /** JWS Verifier for token. */
    private static final JWSVerifier JWS_VERIFIER =
            new MACVerifier(KeyManager.decodeToBytes(SecretKey.SECRET_KEY));

    /**
     * Verifies a JWT provided in the authorization header.
     *
     * @param authHeader The authorization header containing the JWT.
     * @return True if the JWT is valid, false otherwise.
     * @throws RuntimeException If the authorization header
     * is missing or the JWT is invalid.
     */
    public static boolean verify(final String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final int tokenIndex = 7;
            String token = authHeader.substring(tokenIndex);
            try {
                SignedJWT jwtToken = SignedJWT.parse(token);
                if (!jwtToken.verify(JWS_VERIFIER)) {
                    return false;
                }
                JWTClaimsSet claimsSet =
                        (JWTClaimsSet) jwtToken.getJWTClaimsSet();
                Date expirationTime = claimsSet.getExpirationTime();

                if (expirationTime == null
                        || expirationTime.before(new Date())) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Invalid JWT token", e);
            }
        } else {
            throw new RuntimeException("Missing Authorization header");
        }
    }
}
