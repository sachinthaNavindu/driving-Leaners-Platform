package lk.ijse.drivingschool.dto;

import lk.ijse.drivingschool.entity.InstructorRespond;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SessionTimeTableDTO {
    private String sessionId;
    private Timestamp time;
    private LocalDate date;
    private String instructorName;
    private String nic;
    private String vehicleNumber;
    private InstructorRespond respond;
    private String courseName;
}
