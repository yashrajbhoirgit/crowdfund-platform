package com.crowdfund.donation;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigDecimal;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendDonationConfirmation(String toEmail, String donorName, BigDecimal amount, String campaignTitle, String transactionId) {
        if (mailSender == null) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(toEmail);
            helper.setSubject("Donation Confirmation - " + campaignTitle);
            
            String htmlContent = String.format(
                "<h1>Thank you for your donation!</h1>" +
                "<p>Dear %s,</p>" +
                "<p>We have successfully received your donation of ₹%s towards the campaign '<strong>%s</strong>'.</p>" +
                "<p>Transaction ID: %s</p>" +
                "<p>Thank you for making a difference.</p>",
                donorName, amount.toString(), campaignTitle, transactionId
            );
            
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDonationReceiptWithPdf(String toEmail, String donorName, byte[] pdfBytes) {
        if (mailSender == null) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(toEmail);
            helper.setSubject("Your Donation Receipt");
            helper.setText("<p>Dear " + donorName + ",</p><p>Please find attached your donation receipt.</p>", true);
            
            helper.addAttachment("Receipt.pdf", new ByteArrayResource(pdfBytes));
            
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
