package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lk.ijse.drivingschool.entity.enums.ApplicationRespond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nic;
    private String fullName;
    private String email;
    private String address;
    private String contact;
    private String courseName;
    private Double paidAmount;
    private String nicBackUrl;
    private String nicFrontUrl;
    private String paymentSlip;
    @Enumerated(EnumType.STRING)
    private ApplicationRespond respond;
}
