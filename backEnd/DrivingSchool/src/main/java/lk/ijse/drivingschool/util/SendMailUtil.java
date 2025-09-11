package lk.ijse.drivingschool.util;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendMailUtil {

    private static final String username = "navindus485@gmail.com";
    private static final String password = "oere qtjo xrlm beyn";

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void sendEmailAsync(String to, String subject, String body) {
        Runnable emailTask = () -> {
            try {
                sendEmail(to, subject, body);
            } catch (MessagingException e) {
                throw new RuntimeException(e+"Failed to send email");
            }
        };

        executorService.submit(emailTask);
    }

    private static void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body,"text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("Email sent successfully to " + to);
    }
}