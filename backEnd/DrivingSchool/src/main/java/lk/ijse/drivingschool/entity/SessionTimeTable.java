package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionTimeTable {
    @Id
    private String sessionId;

    private Timestamp startTime;
    private LocalDate date;

    @Column(name = "vehicleNumber", nullable = false, insertable = false, updatable = false)
    private String vehicleNumber;

    @Column(name = "licenseId", nullable = false, insertable = false, updatable = false)
    private String licenseId;

    @ManyToOne
    @JoinColumn(name = "licenseId", referencedColumnName = "licenseId",insertable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "vehicleNumber", referencedColumnName = "vehicleNumber")
    private Vehicle vehicle;
}
