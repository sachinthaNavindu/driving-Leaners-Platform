package lk.ijse.drivingschool.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import lk.ijse.drivingschool.dto.PaymentDTO;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.util.ByteArrayDataSource;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SendSlipEmail {

    private static final String username = "navindus485@gmail.com";
    private static final String password = "oere qtjo xrlm beyn";

    public static void sendPaymentSlipEmail(String to, PaymentDTO payment) throws Exception {
        // 1. Generate PDF in memory with enhanced styling
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        // Header with blue background
        canvas.setColorFill(new Color(26, 42, 108)); // #1a2a6c
        canvas.rectangle(0, document.getPageSize().getHeight() - 30, document.getPageSize().getWidth(), 30);
        canvas.fill();

        // Add title text
        canvas.beginText();
        canvas.setColorFill(Color.WHITE);
        canvas.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, false), 20);
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "DriveMaster Driving School",
                document.getPageSize().getWidth() / 2,
                document.getPageSize().getHeight() - 20, 0);
        canvas.endText();

        // Add content using paragraphs for better formatting
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font amountFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font footerFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        // Add spacing
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Payment receipt title
        Paragraph receiptTitle = new Paragraph("PAYMENT RECEIPT", titleFont);
        receiptTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(receiptTitle);

        document.add(new Paragraph(" "));

        // Current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        // Receipt details
        document.add(new Paragraph("Receipt ID: " + (payment.getPaymentID() != null ? payment.getPaymentID() : "N/A"), normalFont));
        document.add(new Paragraph("Date: " + currentDate, normalFont));
        document.add(new Paragraph("Time: " + currentTime, normalFont));

        // Add horizontal line
        document.add(new Paragraph("________________________________________________________________________"));

        document.add(new Paragraph(" "));

        // Payment details
        document.add(new Paragraph("Student NIC: " + (payment.getStudentNic() != null ? payment.getStudentNic() : "N/A"), normalFont));
        document.add(new Paragraph("Course: " + (payment.getCourseName() != null ? payment.getCourseName() : "N/A"), normalFont));
        document.add(new Paragraph("Payment Date: " + (payment.getPaymentDate() != null ? payment.getPaymentDate() : "N/A"), normalFont));
        document.add(new Paragraph("Payment Time: " + (payment.getPaidTime() != null ? payment.getPaidTime() : "N/A"), normalFont));

        // Add horizontal line
        document.add(new Paragraph("________________________________________________________________________"));

        document.add(new Paragraph(" "));

        // Amount paid (bold and prominent)
        String amount = payment.getPaidAmount() != null ?
                String.format("LKR %,.2f", payment.getPaidAmount()) : "LKR 0.00";
        Paragraph amountParagraph = new Paragraph("Amount Paid: " + amount, amountFont);
        amountParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(amountParagraph);

        // Add horizontal line
        document.add(new Paragraph("________________________________________________________________________"));

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Footer messages
        Paragraph thankYou = new Paragraph("Thank you for your payment!", footerFont);
        thankYou.setAlignment(Element.ALIGN_CENTER);
        document.add(thankYou);

        Paragraph slogan = new Paragraph("DriveMaster Driving School - Quality Education for Safe Driving", footerFont);
        slogan.setAlignment(Element.ALIGN_CENTER);
        document.add(slogan);

        document.close();

        byte[] pdfBytes = outputStream.toByteArray();

        // 2. Send Email with attachment
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("DriveMaster Payment Receipt");

        // Body
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent("Dear Student,<br><br>Please find attached your payment receipt.<br><br>Thank you!", "text/html; charset=utf-8");

        // Attachment
        MimeBodyPart pdfPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(pdfBytes, "application/pdf");
        pdfPart.setDataHandler(new DataHandler(source));
        pdfPart.setFileName("Payment_Receipt.pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(pdfPart);

        message.setContent(multipart);

        Transport.send(message);
        System.out.println("Email with slip sent to " + to);
    }
}