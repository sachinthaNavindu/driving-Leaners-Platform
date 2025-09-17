package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.InstructorScheduleDTO;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionTimeTableService {

    private final SessionTimeTableRepo sessionTimeTableRepo;

    public List<InstructorScheduleDTO> getScheduleByInstructor(String licenseId) {
        List<Object[]> results = sessionTimeTableRepo.findInstructorSchedule(licenseId);

        return results.stream()
                .map(r -> new InstructorScheduleDTO(
                        (String) r[0],
                        (Timestamp) r[1],
                        (LocalDate) r[2],
                        (String) r[3],
                        (String) r[4]
                ))
                .toList();
    }
}
