package com.logic.server_newsapp.jwt;

import lombok.Getter;

/**
 * Holder for the secret key used in JWT signing.
 *
 * <p>
 *  This class provides the static `SECRET_KEY` constant
 *  which should be treated as a secret, and not stored in code.
 * </p>
 */
@Getter
public final class SecretKey {
    private SecretKey() {
    }

    /**
     * The secret key used for signing JWTs. <strong>Do not
     * store the key in code</strong>. Use an
     * environment variable or a vault solution instead.
     */
    public static final String SECRET_KEY =
            "jHCIMolt4bMr22mFgA41Dgs3DgUb3LB+XzFQj4pT5po=";
}
