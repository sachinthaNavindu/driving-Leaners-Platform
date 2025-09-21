package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.StudentCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentCredentialRepository extends JpaRepository<StudentCredentials,String> {
    Optional<StudentCredentials> findByStudentNic(String studentNic);

}
