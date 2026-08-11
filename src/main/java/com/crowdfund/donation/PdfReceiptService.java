package com.crowdfund.donation;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfReceiptService {

    public byte[] generateReceipt(Donation donation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(baos);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
            
            document.add(new com.itextpdf.layout.element.Paragraph("CROWDFUND PLATFORM").setBold().setFontSize(20));
            document.add(new com.itextpdf.layout.element.Paragraph("Donation Receipt").setFontSize(16));
            document.add(new com.itextpdf.layout.element.Paragraph("Receipt #: " + donation.getId()));
            document.add(new com.itextpdf.layout.element.Paragraph("Date: " + donation.getDonatedAt().toString()));
            document.add(new com.itextpdf.layout.element.Paragraph("Donor Name: " + (donation.isAnonymous() ? "Anonymous" : donation.getDonorName())));
            document.add(new com.itextpdf.layout.element.Paragraph("Campaign: " + donation.getCampaignTitle()));
            document.add(new com.itextpdf.layout.element.Paragraph("Amount: Rs. " + donation.getAmount().toString()));
            document.add(new com.itextpdf.layout.element.Paragraph("Transaction ID: " + donation.getTransactionId()));
            
            document.close();
            
        } catch (Exception e) {
            // Fallback if iText is missing
            try {
                String fallbackText = "Receipt for Donation ID: " + donation.getId();
                baos.write(fallbackText.getBytes());
            } catch (Exception ex) {}
        }
        return baos.toByteArray();
    }
}
