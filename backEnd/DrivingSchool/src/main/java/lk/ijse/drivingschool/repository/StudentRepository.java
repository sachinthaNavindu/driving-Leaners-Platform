package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student,String> {
    Optional<Student> findByNic(String nic);
}
