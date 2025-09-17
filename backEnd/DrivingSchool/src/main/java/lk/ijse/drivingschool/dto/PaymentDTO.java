package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private String paymentID;
    private LocalDate paymentDate;
    private LocalTime paidTime;
    private Double paidAmount;
    private String courseName;
    private String studentNic;
}
