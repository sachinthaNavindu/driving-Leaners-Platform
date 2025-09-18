package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.StudentCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentCredentialRepository extends JpaRepository<StudentCredentials,String> {
    Optional<StudentCredentials> findByStudentNic(String studentNic);

}
