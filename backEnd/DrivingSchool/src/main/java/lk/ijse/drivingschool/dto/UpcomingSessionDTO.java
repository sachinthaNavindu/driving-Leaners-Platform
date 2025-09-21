package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class UpcomingSessionDTO {
    private String sessionId;
    private String courseName;
    private LocalDate date;
    private String respond;
    private LocalTime time;
    private String vehicleNumber;
    private String instructorName;
}