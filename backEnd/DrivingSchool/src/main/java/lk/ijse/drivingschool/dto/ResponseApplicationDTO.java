package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseApplicationDTO {
    private Long id;
    private String name;
    private String nic;
    private String email;
    private String contact;
    private String address;
    private String courseName;
    private String paidAmount;
    private String nicFrontUrl;
    private String nicBackUrl;
    private String paymentSlip;
}
