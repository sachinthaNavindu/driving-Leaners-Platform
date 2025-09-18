package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,String> {
    Optional<Payment> findByPaymentId(String paymentId);

    @Query("SELECT p.course.courseName FROM Payment p WHERE p.student.nic = :nic")
    String getCourseNameByStudentNic(@Param("nic") String nic);
}
