package lk.ijse.drivingschool.service;

import jakarta.validation.Valid;
import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;

import java.util.List;

public interface StudentSessionService {
    String confirmParticipation(String sessionId, String nic);

    List<UpcomingSessionDTO> getSessionsByStudent(String nic);
}
