package lk.ijse.drivingschool.dto;

import lk.ijse.drivingschool.entity.Instructor;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodaySessionDTO {
    private String sessionTimeTableId;  // from SessionTimeTable
    private String pendingSessionId;     // from PendingSessions
    private Timestamp time;
    private LocalDate date;
    private String licenseId;
    private String vehicleNumber;
    private String respond;
    private String courseName;



}
