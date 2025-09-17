package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InstructorScheduleDTO {
    private String sessionTimeTableId;
    private Timestamp time;
    private LocalDate date;
    private String vehicleNumber;
    private String courseName;
}
