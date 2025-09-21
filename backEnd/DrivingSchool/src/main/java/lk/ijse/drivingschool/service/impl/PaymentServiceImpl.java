package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.InfluentPaymentDTO;
import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.entity.Payment;
import lk.ijse.drivingschool.entity.Student;
import lk.ijse.drivingschool.repository.PaymentRepo;
import lk.ijse.drivingschool.repository.StudentCredentialRepository;
import lk.ijse.drivingschool.repository.StudentRepository;
import lk.ijse.drivingschool.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;
    private final StudentRepository studentRepo;
    private final StudentCredentialRepository studentCredentialRepo;

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

    @Transactional
    public String deletePayment(String paymentId) {
        Payment payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment Id not found"));
        Student student = payment.getStudent();

        paymentRepo.delete(payment);

        if (student.getPayments().isEmpty()) {
            studentRepo.delete(student);
        }

        return "Payment deleted successfully";
    }

    public String getUserCourse(String nic) {

        return paymentRepo.getCourseNameByStudentNic(nic);
    }

    @Override
    public List<InfluentPaymentDTO> getPaymentsWithYetToPay() {
        return paymentRepo.findPaymentsWithCourseFee();
    }
}
