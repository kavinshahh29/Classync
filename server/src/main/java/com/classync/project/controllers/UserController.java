package com.classync.project.controllers;

import lombok.Getter;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.classync.project.entity.User;
import com.classync.project.services.impl.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        try {
            User user = userService.findById(updatedUser.getId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            user.setFullName(updatedUser.getFullName());
            user.setPicture(updatedUser.getPicture());
            userService.saveUser(user);

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

    public record UserDetails(int id, String email, String fullName, String picture) {
    }

}