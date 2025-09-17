package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;

    public List<PaymentDTO> getAllPayment() {
        return paymentRepo.findAll().stream()
                .map(payment -> new PaymentDTO(
                        payment.getPaymentId(),
                        payment.getPaymentDate().toLocalDateTime().toLocalDate(),
                        payment.getPaymentTime().toLocalDateTime().toLocalTime(),
                        payment.getPaymentAmount(),
                        payment.getCourse().getCourseName(),
                        payment.getStudent().getNic()
                ))
                .toList();
    }
}
