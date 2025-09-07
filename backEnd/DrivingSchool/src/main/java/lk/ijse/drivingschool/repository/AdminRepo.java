package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional <Admin> findByNic(String nic);
    }
