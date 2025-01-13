package com.backend.backend.utils;

import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleTokenValidator {

    private static final String CLIENT_ID = "805330733809-gvoq4nc6c2qgibbqm5b2q0t6rtqle39b.apps.googleusercontent.com";

    public static void validateToken(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance()
        ).setAudience(Collections.singletonList(CLIENT_ID)).build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Validate claims
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);
            System.out.println("Email: " + payload.getEmail());
        } else {
            throw new IllegalArgumentException("Invalid ID token");
        }
    }
}
