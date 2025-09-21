package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDTO {

    @NotBlank(message = "NIC is required")
    @Pattern(regexp = "^(?:\\d{9}[VvXx]|\\d{12})$")
    private String nic;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3–50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "^(?:0|94)?7[0-9]{8}$",message = "Contact must be a valid Sri Lankan mobile number")
    private String contact;
}
