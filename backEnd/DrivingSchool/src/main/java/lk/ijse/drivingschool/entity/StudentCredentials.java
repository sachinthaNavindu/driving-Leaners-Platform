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
public class StudentCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(name = "student_nic", nullable = false, unique = true)
    private String studentNic;
    private String password;

    @OneToOne
    @JoinColumn(name = "student_nic", referencedColumnName = "nic", insertable = false, updatable = false)
    private Student student; // ORM reference

}
