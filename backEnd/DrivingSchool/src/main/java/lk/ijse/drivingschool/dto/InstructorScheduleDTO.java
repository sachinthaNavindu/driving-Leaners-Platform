package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class InstructorScheduleDTO {
    @NotBlank(message = "Session timetable ID cannot be blank")
    private String sessionTimeTableId;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    @NotBlank(message = "Vehicle number cannot be blank")
    @Pattern(regexp = "^[A-Z]{1,2}-\\d{4}$", message = "Vehicle Id must be like WP-1234")
    private String vehicleNumber;

    @NotBlank(message = "Course name cannot be blank")
    @Size(max = 50, message = "Course name must be at most 50 characters")
    private String courseName;
}
