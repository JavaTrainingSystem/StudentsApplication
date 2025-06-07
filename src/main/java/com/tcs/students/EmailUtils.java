package com.tcs.students;

import com.tcs.students.dto.EmailPayload;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {


    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailPayload emailPayload) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("javatrainingsystems@gmail.com");
            helper.setTo(emailPayload.getTo().toArray(new String[0]));
            helper.setSubject(emailPayload.getSubject());
            helper.setText(emailPayload.getBody(), true); // set to true if you're sending HTML content
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Email sent successfully!");
    }
}