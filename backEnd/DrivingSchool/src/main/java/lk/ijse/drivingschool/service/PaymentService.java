package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.InfluentPaymentDTO;
import lk.ijse.drivingschool.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAllPayment();
    String deletePayment(String paymentId);
    String getUserCourse(String nic);

    List<InfluentPaymentDTO> getPaymentsWithYetToPay();
}
