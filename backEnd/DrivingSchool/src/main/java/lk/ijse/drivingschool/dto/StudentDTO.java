package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDTO {
    private String nic;
    private String name;
    private String email;
    private String address;
    private String contact;
}
