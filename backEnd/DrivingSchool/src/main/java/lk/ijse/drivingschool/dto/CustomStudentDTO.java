package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomStudentDTO {
    private long registeredStudentCount;
    private long activeStudentCount;
    private long newApplicationCount;
}
