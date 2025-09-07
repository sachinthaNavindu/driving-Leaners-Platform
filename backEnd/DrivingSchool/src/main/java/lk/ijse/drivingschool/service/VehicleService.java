package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.VehicleDTO;
import lk.ijse.drivingschool.entity.Vehicle;
import lk.ijse.drivingschool.repository.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepo vehicleRepo;
    public Object getAvailableVehicles() {
        List<Vehicle>availableVehicles = vehicleRepo.findByAvailability("Available");

        return availableVehicles.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getVehicleNumber(),
                        vehicle.getVehicleType(),
                        vehicle.getAvailability()
                ));
    }
}
