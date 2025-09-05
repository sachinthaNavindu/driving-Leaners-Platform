package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAuthDTO {
    private String nic;
    private String email;
    private String password;
}
