package com.classync.project.security;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private final Dotenv dotenv = Dotenv.load();

    public String getDatabaseUrl() {
        return dotenv.get("DATABASE_URL");
    }

    public String getDatabaseUser() {
        return dotenv.get("DATABASE_USER");
    }

    public String getDatabasePassword() {
        return dotenv.get("DATABASE_PASSWORD");
    }

    public String getGoogleClientId() {
        return dotenv.get("GOOGLE_CLIENT_ID");
    }

    public String getGoogleClientSecret() {
        return dotenv.get("GOOGLE_CLIENT_SECRET");
    }
}
