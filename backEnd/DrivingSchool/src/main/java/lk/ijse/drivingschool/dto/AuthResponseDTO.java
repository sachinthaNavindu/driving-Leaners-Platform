package lk.ijse.drivingschool.dto;

import lk.ijse.drivingschool.entity.enums.JobRole;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String nic;
    private String username;
    private JobRole jobRole;
    private String licenseId;
}
