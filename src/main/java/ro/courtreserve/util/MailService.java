package ro.courtreserve.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String recipientEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Reservation Receipt");
        mailMessage.setText("reservation confirmed!");
        mailSender.send(mailMessage);
    }
}
