package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.InstructorScheduleDTO;
import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.entity.PendingSessions;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lk.ijse.drivingschool.service.SessionTimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionTimeTableServiceImpl implements SessionTimeTableService {

    private final SessionTimeTableRepo sessionTimeTableRepo;

    public List<InstructorScheduleDTO> getScheduleByInstructor(String licenseId) {
        List<Object[]> results = sessionTimeTableRepo.findInstructorSchedule(licenseId);

        return results.stream()
                .map(r -> new InstructorScheduleDTO(
                        (String) r[0],
                        (LocalTime) r[1],
                        (LocalDate) r[2],
                        (String) r[3],
                        (String) r[4]
                ))
                .toList();
    }

    public  List<UpcomingSessionDTO> getUpcomingSessions(String userCourse,String nic) {
        List<SessionTimeTable> sessions = sessionTimeTableRepo.findUpcomingSessionsForStudent(
                userCourse,
                nic,
                LocalDate.now().plusDays(1),
                PageRequest.of(0, 1)
        );

        return sessions.stream().map(s -> new UpcomingSessionDTO(
                s.getSessionId(),
                s.getPendingSession().getCourseName(),
                s.getPendingSession().getDate(),
                s.getPendingSession().getRespond() != null ? s.getPendingSession().getRespond().name() : "PENDING",
                s.getPendingSession().getTime(),
                s.getPendingSession().getVehicle() != null ? s.getPendingSession().getVehicle().getVehicleNumber() : "Unknown",
                s.getPendingSession().getInstructor() != null &&
                        s.getPendingSession().getInstructor().getEmployee() != null
                        ? s.getPendingSession().getInstructor().getEmployee().getName()
                        : "Not Assigned"
        )).toList();
    }

    @Override
    public long getCountTodaysSession() {
        return sessionTimeTableRepo.countTodaysSessions(LocalDate.now());
    }


    @Override
    public List<PendingSessionDTO> getAllSessionsFromToday() {
        LocalDate today = LocalDate.now();
        List<SessionTimeTable> sessions = sessionTimeTableRepo.findApprovedSessionsFromToday(today);

        return sessions.stream().map(st -> {
            PendingSessions ps = st.getPendingSession();

            return PendingSessionDTO.builder()
                    .sessionId(ps.getSessionId())
                    .time(ps.getTime())
                    .date(ps.getDate())
                    .licenseId(ps.getInstructor() != null ? ps.getInstructor().getLicenseId() : null)
                    .vehicleNumber(ps.getVehicle().getVehicleNumber())
                    .respond(ps.getRespond() != null ? ps.getRespond().name() : null)
                    .courseName(ps.getCourseName())
                    .build();
        }).collect(Collectors.toList());
    }




}
