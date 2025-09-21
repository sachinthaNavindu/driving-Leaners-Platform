package lk.ijse.drivingschool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseDTO {
    @NotBlank(message = "CourseName is required")
    private String courseName;
    @NotNull(message = "Course Fee is required")
    private Double courseFee;
    @Min(value = 1, message = "Sessions must be at least 1")
    private int sessions;
}
