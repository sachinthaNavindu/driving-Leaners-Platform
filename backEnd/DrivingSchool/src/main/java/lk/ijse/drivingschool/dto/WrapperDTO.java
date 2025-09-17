package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrapperDTO {
    private StudentDTO studentDTO;
    private PaymentDTO paymentDTO;
}
