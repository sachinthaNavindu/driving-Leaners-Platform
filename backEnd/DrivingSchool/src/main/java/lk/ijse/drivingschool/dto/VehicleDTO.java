package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleDTO {
    private String vehicleNumber;
    private String vehicleType;
    private String availability;
}
