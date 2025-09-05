package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAuthDTO {
    private String nic;
    private String username;
    private String gmail;
    private String password;
}
