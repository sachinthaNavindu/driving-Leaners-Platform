package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepo extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByAvailability(String available);
}
