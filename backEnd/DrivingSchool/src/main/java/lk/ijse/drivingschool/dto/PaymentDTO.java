package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    @NotNull(message = "Paid time is required")
    private LocalTime paidTime;

    @NotNull(message = "Paid amount is required")
    @Positive(message = "Paid amount must be greater than 0")
    private Double paidAmount;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Student NIC is required")
    @Pattern(regexp = "^(\\d{9}[VvXx]|\\d{12})$",message = "Student NIC must be a valid Sri Lankan NIC (old or new format)")
    private String studentNic;
}
