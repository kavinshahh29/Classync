// package com.classync.project.controllers;

// import com.classync.project.services.MailService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.io.IOException;

// @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
// @RestController
// @RequestMapping("/api/mail")
// public class MailController {

//     @Autowired
//     private MailService mailService;

//     @PostMapping("/send")
//     public String sendMail(
//             @RequestParam String to,
//             @RequestParam String subject,
//             @RequestParam String message) {
//         try {
//             return mailService.sendMail(to, subject, message);
//         } catch (IOException e) {
//             return "Error sending mail: " + e.getMessage();
//         }
//     }
// }

// MailController.java
package com.classync.project.controllers;

import com.classync.project.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody Map<String, String> emailRequest) {
        try {
            String to = emailRequest.get("to");
            String subject = emailRequest.get("subject");
            String message = emailRequest.get("message");

            String result = mailService.sendMail(to, subject, message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending mail: " + e.getMessage());
        }
    }
}