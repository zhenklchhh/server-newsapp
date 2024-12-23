package com.logic.server_newsapp.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.extern.log4j.Log4j2;

/** Utility class for managing keys. */
@Log4j2
public final class KeyManager {

    /** Directory with keys file. */
    private static final String KEYS_DIRECTORY = "src/main/resources/keys/";

    /** File name with secure key. */
    private static final String ENV_FILE_NAME = "secure_key";

    /** Secret key env var. */
    private static final String SECRET_KEY_ENV_VAR = "SECRET_KEY";

    private KeyManager() {
    }

    /**
     * Generates a new secret key, encodes
     * it with Base64, and writes it to a file.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static void generateSecretKey() throws IOException {
        final int keyLength = 32;
        byte[] keyBytes = new byte[keyLength];
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        Path envFile = Paths.get(KEYS_DIRECTORY, ENV_FILE_NAME);
        Files.writeString(envFile, SECRET_KEY_ENV_VAR + "=" + base64Key);
        log.info("Generated secret key and saved to file: "
                + envFile.toAbsolutePath());
    }

    /**
     * Decodes a Base64 encoded secret key into a byte array.
     *
     * @param secret The Base64 encoded secret key.
     * @return The decoded byte array representing the secret key.
     */
    public static byte[] decodeToBytes(final String secret) {
        return Base64.getDecoder().decode(secret);
    }

    /**
     * Retrieves the secret key from environment file,
     * decodes it with Base64, and returns it as a byte
     * array.
     *
     * @return The secret key as a byte array.
     * @throws IOException If an I/O error occurs.
     */
    public static byte[] getSecretKeyBytes() throws IOException {
        String base64Key = readSecretKeyFromEnv();
        return Base64.getDecoder().decode(base64Key);
    }

    /**
     * Reads the secret key from the environment file.
     *
     * @return The secret key.
     * @throws IOException If an I/O error occurs.
     */
    private static String readSecretKeyFromEnv() throws IOException {
        Path envFilePath = Paths.get(KEYS_DIRECTORY, ENV_FILE_NAME);
        try {
            String envContent = Files.readString(envFilePath);
            String[] envVars = envContent.split("\n");

            for (String envVar : envVars) {
                String[] parts = envVar.split("=", 2);
                if (parts.length == 2
                        && parts[0].trim().equals(SECRET_KEY_ENV_VAR)) {
                    return parts[1].trim();
                }
            }
            throw new RuntimeException(
                    "Secret key not found in " + ENV_FILE_NAME);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + ENV_FILE_NAME, e);
        }
    }
}
