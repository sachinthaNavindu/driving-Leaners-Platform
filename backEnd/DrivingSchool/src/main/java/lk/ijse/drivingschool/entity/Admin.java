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
public class  Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(name = "employee_nic",nullable = false,unique = true)
    private String nic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_nic",referencedColumnName = "nic",insertable = false,updatable = false)
    private Employee employee;


}
