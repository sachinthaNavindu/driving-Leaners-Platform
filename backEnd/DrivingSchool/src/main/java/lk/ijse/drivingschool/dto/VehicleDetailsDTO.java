package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VehicleDetailsDTO {
    List<VehicleDTO> vehicles;
    long availableVehicleCount;
    long unavailableVehicleCount;
}
