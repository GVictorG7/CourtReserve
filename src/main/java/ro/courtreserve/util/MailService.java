package ro.courtreserve.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.model.dto.SubscriptionDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Component
@RequiredArgsConstructor
public class MailService {
    public static final String RECEIPT_PATH = "src/main/resources/receipts/";
    private final JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, ReservationDTO reservation, Float price) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(recipientEmail);
            message.setSubject("Reservation Receipt");
            message.setText("");
            String date = reservation.getDay() + "." + reservation.getMonth() + "." + reservation.getYear();
            String receiptMessage = "Your reservation with the code " + reservation.getId()
                    + " was confirmed. Please come to the court " + reservation.getCourtId() + " on "
                    + date + " at "
                    + reservation.getHour() + ". Total price: " + price;

            String receiptName = generatePDF(receiptMessage, recipientEmail, date);

            FileSystemResource file = new FileSystemResource(new File(RECEIPT_PATH + receiptName));
            message.addAttachment(receiptName, file);
        };

        mailSender.send(preparator);
    }

    public void sendEmail(String recipientEmail, SubscriptionDTO subscription, Float price) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(recipientEmail);
            message.setSubject("Subscription Receipt");
            message.setText("");
            String date = subscription.getStartDateDay() + "." + subscription.getStartDateMonth() + "." + subscription.getStartDateYear();
            String receiptMessage = "Your subscription with the code " + subscription.getId() +
                    " was confirmed. You can come to the court " + subscription.getCourtId() +
                    " every day starting from " + date + " between " + subscription.getStartHour() + " and " +
                    subscription.getEndHour() + ". Total price: " + price;

            String receiptName = generatePDF(receiptMessage, recipientEmail, date);

            FileSystemResource file = new FileSystemResource(new File(RECEIPT_PATH + receiptName));
            message.addAttachment(receiptName, file);
        };

        mailSender.send(preparator);
    }

    private String generatePDF(String receiptMessage, String recipientEmail, String date) {
        String receiptName = "receipt_" + recipientEmail + "_" + date + ".pdf";
        try {
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(RECEIPT_PATH + receiptName));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            Text text = new Text(receiptMessage);
            document.add(new Paragraph(text));
            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return receiptName;
    }
}
