package com.classync.project.controllers;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173" , allowedHeaders = "*" , allowCredentials = "true")
public class UserController {

    @GetMapping("/api/user")
    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        return ResponseEntity.ok(new UserDetails(
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                oidcUser.getPicture()
        ));
    }

    @Getter
    public static class UserDetails {
        private final String email;
        private final String fullName;
        private final String picture;

        public UserDetails(String email, String fullName, String picture) {
            this.email = email;
            this.fullName = fullName;
            this.picture = picture;
        }

    }
}