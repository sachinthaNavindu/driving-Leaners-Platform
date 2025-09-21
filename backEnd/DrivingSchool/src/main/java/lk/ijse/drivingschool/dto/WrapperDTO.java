package lk.ijse.drivingschool.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrapperDTO {
    @Valid
    private StudentDTO studentDTO;
    @Valid
    private PaymentDTO paymentDTO;
}
