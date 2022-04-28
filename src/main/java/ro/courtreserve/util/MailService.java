package ro.courtreserve.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.model.dto.SubscriptionDTO;

@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, ReservationDTO reservation, Float price) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Reservation Receipt");
        mailMessage.setText("Your reservation with the code " + reservation.getId()
                + " was confirmed. Please come to the court " + reservation.getCourtId() + " on "
                + reservation.getDay() + "." + reservation.getMonth() + "." + reservation.getYear() + " at "
                + reservation.getHour() + ". Total price: " + price);
        mailSender.send(mailMessage);
    }

    public void sendEmail(String recipientEmail, SubscriptionDTO subscription, Float price) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Subscription Receipt");
        mailMessage.setText("Your subscription with the code " + subscription.getId()
                + " was confirmed. You can come to the court " + subscription.getCourtId() + " every day starting from "
                + subscription.getStartDateDay() + "." + subscription.getStartDateMonth() + "."
                + subscription.getStartDateYear() + " between " + subscription.getStartHour() + " and "
                + subscription.getEndHour() + ". Total price: " + price);
        mailSender.send(mailMessage);
    }
}
