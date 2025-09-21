package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAuthDTO {

    @NotBlank(message = "NIC is required")
    @Pattern(regexp = "^(?:\\d{9}[VvXx]|\\d{12})$")
    private String nic;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4–20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String gmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}

