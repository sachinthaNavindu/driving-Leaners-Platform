package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepo extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByAvailability(String available);

    long countAllByAvailability(String availability);

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);
}
