package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstructorDTO {
    @Pattern(regexp = "^(\\d{7}[Vv]|\\d{12})$",message = "License ID must be valid (Old: 1234567V or New: 200012345678)")
    private String licenseId;
    @NotBlank(message = "Name is Required")
    private String name;
}
