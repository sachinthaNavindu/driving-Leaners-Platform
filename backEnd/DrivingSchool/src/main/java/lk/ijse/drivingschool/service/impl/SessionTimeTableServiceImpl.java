package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.InstructorScheduleDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lk.ijse.drivingschool.service.SessionTimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionTimeTableServiceImpl implements SessionTimeTableService {

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

    public UpcomingSessionDTO getUpcomingSessions(String userCourse) {
        return sessionTimeTableRepo
                .findTopByPendingSession_CourseNameAndPendingSession_RespondAndPendingSession_DateGreaterThanEqualOrderByPendingSession_DateAsc(
                        userCourse,
                        InstructorRespond.ACCEPTED,
                        LocalDate.now()
                )
                .map(s -> new UpcomingSessionDTO(
                        s.getSessionId(),
                        s.getPendingSession().getCourseName(),
                        s.getPendingSession().getDate(),
                        s.getPendingSession().getRespond().name(),
                        s.getPendingSession().getTime(),
                        s.getPendingSession().getVehicleNumber(),
                        s.getPendingSession().getInstructor().getEmployee().getName()
                ))
                .orElse(null);
    }


}
