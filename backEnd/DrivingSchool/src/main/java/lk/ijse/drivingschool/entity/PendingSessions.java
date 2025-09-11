package lk.ijse.drivingschool.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PendingSessions {
    @Id
    private String sessionId;
    private Timestamp time;
    private LocalDate date;
    private String instructorName;
    private String nic;
    private String vehicleNumber;
    @Enumerated(EnumType.STRING)
    private InstructorRespond respond;
    private String courseName;

}
