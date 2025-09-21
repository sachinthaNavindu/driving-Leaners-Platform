package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class StudentApplicationDTO {
    private Long id;
    private String name;
    private String nic;
    private String email;
    private String contact;
    private String address;
    private String courseName;
    private String paidAmount;
    private MultipartFile nicFront;
    private MultipartFile nicBack;
    private MultipartFile paymentSlip;
}