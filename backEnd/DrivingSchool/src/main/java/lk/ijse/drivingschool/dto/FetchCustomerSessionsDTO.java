package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class FetchCustomerSessionsDTO {
    private String sessionId;
    private LocalDate date;
    private LocalTime time;
    private String licenseId;
    private String vehicleNumber;
    private String courseName;
}
