package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstructorAuthDTO {
    @Pattern(regexp = "^(\\d{7}[Vv]|\\d{12})$",message = "License ID must be valid (Old: 1234567V or New: 200012345678)")
    private String licenseId;
    @Pattern(regexp = "^(\\d{9}[VX]|\\d{12})$",message = "NIC must be valid (Old: 123456789V or New: 200012345678)")
    private String nic;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
