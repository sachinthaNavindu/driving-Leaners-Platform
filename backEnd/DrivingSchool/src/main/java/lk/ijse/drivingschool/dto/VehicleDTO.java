package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleDTO {
    @NotBlank(message = "Vehicle Id is required")
    @Pattern(regexp = "^[A-Z]{1,2}-\\d{4}$", message = "Vehicle Id must be like WP-1234")
    private String vehicleNumber;
    @NotBlank(message = "Type is Required")
    private String vehicleType;
    @NotBlank(message = "Availability is Required")
    private String availability;
}
