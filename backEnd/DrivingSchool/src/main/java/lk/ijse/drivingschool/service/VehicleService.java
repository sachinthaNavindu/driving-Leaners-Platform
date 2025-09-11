package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.VehicleDTO;
import lk.ijse.drivingschool.dto.VehicleDetailsDTO;
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

    public Object getVehicleData() {
        List<Vehicle>vehicles = vehicleRepo.findAll();
        long availableVehicleCount = vehicleRepo.countAllByAvailability(("Available"));
        long unavailableVehicleCount = vehicleRepo.countAllByAvailability("Unavailable");

        List<VehicleDTO> vehicleDTOs = vehicles.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getVehicleNumber(),
                        vehicle.getVehicleType(),
                        vehicle.getAvailability()
                ))
                .toList();

        return new VehicleDetailsDTO(vehicleDTOs,availableVehicleCount,unavailableVehicleCount);
    }

    public String updateVehicle(String updateVehicleNum, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepo.findByVehicleNumber(updateVehicleNum).orElseThrow(()->new RuntimeException("Vehicle not found by this Numberplate"));

        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setAvailability(vehicleDTO.getAvailability());
        vehicleRepo.save(vehicle);

        return "Vehicle updated successfully";
    }

        public String deleteVehicle(String vehicleNum) {
        Vehicle vehicle = vehicleRepo.findByVehicleNumber(vehicleNum).orElseThrow(()->new RuntimeException("Vehicle not found by this Numberplate"));
        vehicle.setVehicleType(vehicleNum);
        vehicleRepo.delete(vehicle);
        return "Vehicle deleted successfully";
    }

    public String saveVehicle(VehicleDTO vehicleDTO) {
        if (vehicleRepo.existsByVehicleNumber(vehicleDTO.getVehicleNumber())){
            throw new RuntimeException("Vehicle number already exists");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(vehicleDTO.getVehicleNumber());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setAvailability(vehicleDTO.getAvailability());

        vehicleRepo.save(vehicle);

        return "Vehicle saved successfully";
    }
}
