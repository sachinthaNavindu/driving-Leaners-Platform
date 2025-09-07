package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instructor {

    @Id
    private String licenseId;

    @Column(name="employee_nic",nullable=false,unique = true)
    private String nic;

    private String password;
    @Enumerated(EnumType.STRING)
    private InstructorStatus status;

    @OneToOne
    @JoinColumn(name = "employee_nic",referencedColumnName = "nic",insertable = false,updatable = false)
    private Employee employee;
}
