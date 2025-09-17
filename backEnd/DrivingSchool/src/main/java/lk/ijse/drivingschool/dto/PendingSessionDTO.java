package lk.ijse.drivingschool.dto;

import lk.ijse.drivingschool.entity.Instructor;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PendingSessionDTO {
    private String sessionId;
    private Timestamp time;
    private LocalDate date;
    private String licenseId;
    private String vehicleNumber;
    private String respond;
    private String courseName;
    private String instructorName;
}

