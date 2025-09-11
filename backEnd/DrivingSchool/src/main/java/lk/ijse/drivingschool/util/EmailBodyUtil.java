package lk.ijse.drivingschool.util;

public class EmailBodyUtil {
    public static String generateEmailBody(String otp) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #4CAF50;'>Your OTP Code</h2>" +
                "<p style='font-size: 16px;'>Dear User,</p>" +
                "<p style='font-size: 16px;'>Your One-Time Password (OTP) is:</p>" +
                "<h1 style='font-size: 36px; color: #FF5733;'>" + otp + "</h1>" +
                "<p style='font-size: 16px;'>Please use this code to complete your verification.</p>" +
                "<p style='font-size: 16px;'>Thank you!</p>" +
                "<footer style='font-size: 12px; color: #888;'>This is an automated message, please do not reply.</footer>" +
                "</body>" +
                "</html>";
    }
}
