package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionInfoDTO {
    private String nextSessionId;
    private long pendingCount;
    private long ApprovedCount;
}
