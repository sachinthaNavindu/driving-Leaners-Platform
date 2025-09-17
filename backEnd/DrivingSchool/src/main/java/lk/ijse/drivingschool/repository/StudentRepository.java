package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student,String> {
    Optional<Student> findByNic(String nic);
    @Transactional
    @Modifying
    int deleteByNic(String nic);
}
