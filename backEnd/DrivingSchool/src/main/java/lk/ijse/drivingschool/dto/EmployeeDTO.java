package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDTO {
    private String nic;
    private String address;
    private String contact;
    private String email;
    private String name;
}
