package com.classync.project.controllers;

import lombok.Getter;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classync.project.entity.User;
import com.classync.project.services.impl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        Optional<User> userOpt = userService.findUserByEmail(oidcUser.getEmail());
        User user = userOpt.get();
        return ResponseEntity.ok(new UserDetails(
                user.getId(),
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                oidcUser.getPicture()));
    }

    @Getter
    public static class UserDetails {
        private final int id;
        private final String email;
        private final String fullName;
        private final String picture;

        public UserDetails(int id, String email, String fullName, String picture) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.picture = picture;
        }

    }
}