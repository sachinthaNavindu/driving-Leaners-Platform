package lk.ijse.drivingschool.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Admin@123"; // your desired password
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
