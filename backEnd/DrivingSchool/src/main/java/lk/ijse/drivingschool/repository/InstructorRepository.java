package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor,String> {
    Optional<Instructor> findByEmployee_Nic(String nic);
    boolean existsByNicOrLicenseId(String nic, String licenseId);


}
