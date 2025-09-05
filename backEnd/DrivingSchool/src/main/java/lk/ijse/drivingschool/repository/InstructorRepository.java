package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,String> {
    Optional<Instructor> findByEmployee_Nic(String nic);
    boolean existsByNicOrLicenseId(String nic, String licenseId);
}
