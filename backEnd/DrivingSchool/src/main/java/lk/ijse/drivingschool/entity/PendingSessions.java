package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PendingSessions {

    @Id
    private String sessionId;

    private LocalTime time;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private InstructorRespond respond;

    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "licenseId", referencedColumnName = "licenseId")
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicleNumber",referencedColumnName = "vehicleNumber",foreignKey = @ForeignKey(name = "fk_pending_vehicle"))
    private Vehicle vehicle;

}
