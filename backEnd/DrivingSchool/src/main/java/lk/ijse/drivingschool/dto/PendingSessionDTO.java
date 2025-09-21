package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class PendingSessionDTO {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    @Pattern(regexp = "^(\\d{7}[Vv]|\\d{12})$",message = "License ID must be valid (Old: 1234567V or New: 200012345678)")
    private String licenseId;

    @NotBlank(message = "Vehicle number is required")
    @Pattern(
            regexp = "^[A-Z]{2,3}-\\d{4}$",
            message = "Vehicle number must be like WP-1234 or ABC-1234"
    )
    private String vehicleNumber;

    @NotBlank(message = "Respond field is required")
    private String respond;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Instructor name is required")
    private String instructorName;
}
