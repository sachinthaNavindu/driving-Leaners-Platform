package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment,String> {
}
