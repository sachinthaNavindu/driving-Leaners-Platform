package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDTO {
    @Pattern(regexp = "^(\\d{9}[VX]|\\d{12})$",message = "NIC must be valid (Old: 123456789V or New: 200012345678)")
    private String nic;
    @NotBlank(message = "Address is required")
    private String address;
    @Pattern(regexp = "^(07\\d{8}|0[1-9]{2}\\d{7})$", message = "Contact number must be a valid Sri Lankan phone number")
    private String contact;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Name is required")
    private String name;
}
