package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.dto.InfluentPaymentDTO;
import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,String> {
    Optional<Payment> findByPaymentId(String paymentId);

    @Query("SELECT p.course.courseName FROM Payment p WHERE p.student.nic = :nic")
    String getCourseNameByStudentNic(@Param("nic") String nic);

    @Query("SELECT new lk.ijse.drivingschool.dto.InfluentPaymentDTO(" +
            "p.paymentId, " +
            "CAST(p.paymentDate AS LocalDate), " +
            "CAST(p.paymentTime AS LocalTime), " +
            "p.paymentAmount, " +
            "c.courseName, " +
            "s.nic, " +
            "(c.courseFee - p.paymentAmount)) " +
            "FROM Payment p " +
            "JOIN p.course c " +
            "JOIN p.student s")
    List<InfluentPaymentDTO> findPaymentsWithCourseFee();
}
