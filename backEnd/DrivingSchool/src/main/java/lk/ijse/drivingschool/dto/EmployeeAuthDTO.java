package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAuthDTO {
    @Pattern(regexp = "^(\\d{9}[VX]|\\d{12})$",message = "NIC must be valid (Old: 123456789V or New: 200012345678)")
    private String nic;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
