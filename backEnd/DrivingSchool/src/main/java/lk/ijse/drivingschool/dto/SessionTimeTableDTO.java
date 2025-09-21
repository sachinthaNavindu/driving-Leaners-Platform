package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class SessionTimeTableDTO {
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    @NotNull(message = "Time is required")
    private LocalTime time;
    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private LocalDate date;
    @NotBlank(message = "License Id is required")
    private String licenseId;
    @NotBlank(message = "Vehicle Id is required")
    @Pattern(regexp = "^[A-Z]{1,2}-\\d{4}$", message = "Vehicle Id must be like WP-1234")
    private String vehicleNumber;
    @NotBlank(message = "Respond cannot be null")
    private String respond;
    @NotBlank(message = "Course name is required")
    private String courseName;
}

