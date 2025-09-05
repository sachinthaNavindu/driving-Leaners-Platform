package lk.ijse.drivingschool.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    private String nic;
    private String name;
    private String email;
    private String address;
    private String contact;

}
