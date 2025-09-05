package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Payment {
    @Id
    private String paymentId;
    private Timestamp paymentDate;
    private Timestamp paymentTime;
    private Double paymentAmount;

    @ManyToOne
    @JoinColumn(name = "student_nic",referencedColumnName = "nic",nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id",referencedColumnName = "courseId",nullable = false)
    private Course course;
}
