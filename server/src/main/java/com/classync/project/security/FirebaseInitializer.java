package com.classync.project.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class FirebaseInitializer {

    public FirebaseInitializer(Environment env) throws IOException {
        String base64Creds = env.getProperty("FIREBASE_API_KEY");

        if (base64Creds != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Creds);
            ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }
}
