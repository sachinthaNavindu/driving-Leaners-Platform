package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class InfluentPaymentDTO {
    private String paymentID;
    private LocalDate paymentDate;
    private LocalTime paidTime;
    private Double paidAmount;
    private String courseName;
    private String studentNic;
    private Double influentAmount;
}
