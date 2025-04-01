
package com.classync.project.services;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.sender.email}")
    private String senderEmail;

    public String sendMail(String to, String subject, String message) {
        try {
            System.out.println("Inside mail service...");
            System.out.println("Sending to: " + to);

            Resend resend = new Resend(apiKey);
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from(senderEmail)

                    .to(to)
                    .html("<strong>" + message + "</strong>")
                    .subject(subject)
                    .build();

            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            System.out.println("Response: " + data.toString());
            return "Mail Sent Successfully!";
        } catch (Exception e) {
            e.printStackTrace(); // Log error stack trace
            return "Failed to send email: " + e.getMessage();
        }
    }

}