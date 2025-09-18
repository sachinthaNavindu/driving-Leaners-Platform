package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.dto.SessionInfoDTO;
import lk.ijse.drivingschool.dto.SessionTimeTableDTO;
import lk.ijse.drivingschool.dto.TodaySessionDTO;
import lk.ijse.drivingschool.entity.Instructor;
import lk.ijse.drivingschool.entity.PendingSessions;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lk.ijse.drivingschool.repository.InstructorRepository;
import lk.ijse.drivingschool.repository.PendingSessionRepo;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lk.ijse.drivingschool.service.PendingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendingSessionServiceImpl implements PendingSessionService {

    private final PendingSessionRepo pendingSessionRepo;
    private final InstructorRepository instructorRepo;
    private final SessionTimeTableRepo sessionTimeTableRepo;

    public Object generateSessionId() {
        String lastId = pendingSessionRepo.getLastSessionId();
        String nextId;
        if (lastId == null) {
            nextId = "S001";
        }else {
            int numericId = Integer.parseInt(lastId.substring(1));
            nextId = String.format("S%03d", numericId+1);
        }

        long pendingCount = pendingSessionRepo.countAllByRespond(InstructorRespond.PENDING);

        long ApprovedCount = pendingSessionRepo.countAllByRespond(InstructorRespond.ACCEPTED);

        return new SessionInfoDTO(nextId, pendingCount, ApprovedCount);

    }

    public String savePendingSession(SessionTimeTableDTO sessionTimeTableDTO) {
        if (pendingSessionRepo.existsBySessionId(sessionTimeTableDTO.getSessionId())) {
            throw new RuntimeException("Session ID already exists");
        }

        Instructor instructor = instructorRepo.findById(sessionTimeTableDTO.getLicenseId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with license ID: "
                        + sessionTimeTableDTO.getLicenseId()));

        PendingSessions pendingSession = PendingSessions.builder()
                .sessionId(sessionTimeTableDTO.getSessionId())
                .time(sessionTimeTableDTO.getTime())
                .date(sessionTimeTableDTO.getDate())
                .vehicleNumber(sessionTimeTableDTO.getVehicleNumber())
                .courseName(sessionTimeTableDTO.getCourseName())
                .respond(InstructorRespond.PENDING)  // default status
                .instructor(instructor)
                .build();
        pendingSessionRepo.save(pendingSession);
        return "Session Saved Successfully";
    }

    public List<PendingSessionDTO> getAllSessions() {
        List<PendingSessions> sessions = pendingSessionRepo.findAll();

        return sessions.stream().map(session -> PendingSessionDTO.builder()
                .sessionId(session.getSessionId())
                .licenseId(session.getInstructor().getLicenseId())
                .date(session.getDate())
                .time(session.getTime())
                .vehicleNumber(session.getVehicleNumber())
                .courseName(session.getCourseName())
                .respond(session.getRespond().name())
                .instructorName(
                        session.getInstructor() != null && session.getInstructor().getEmployee() != null
                                ? session.getInstructor().getEmployee().getName()
                                : "N/A"
                )
                .build()
        ).toList();
    }

    public List<PendingSessionDTO> getAllSessionsForUser(String licenseId) {
        List<PendingSessions> sessions = pendingSessionRepo.findByInstructor_LicenseIdAndRespond(licenseId,InstructorRespond.PENDING);

        return sessions.stream().map(session -> PendingSessionDTO.builder()
                .sessionId(session.getSessionId())
                .licenseId(session.getInstructor().getLicenseId())
                .date(session.getDate())
                .time(session.getTime())
                .vehicleNumber(session.getVehicleNumber())
                .courseName(session.getCourseName())
                .respond(String.valueOf(session.getRespond()))
                .instructorName("")
                .build()
        ).toList();
    }

    public String manageRequest(String action, String nic, String sessionId) {
        PendingSessions pendingSessions = pendingSessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session ID does not exist"));

        pendingSessions.setRespond(InstructorRespond.valueOf(action.toUpperCase()));
        pendingSessionRepo.save(pendingSessions);

        if ("ACCEPTED".equalsIgnoreCase(action)) {
            SessionTimeTable sessionTime = new SessionTimeTable();
            sessionTime.setSessionId(generateNewSessionTimeTableId());

            PendingSessions pendingSession = pendingSessionRepo.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Pending session not found"));

            sessionTime.setPendingSession(pendingSession);
            sessionTimeTableRepo.save(sessionTime);
        }
        return "Action completed successfully";
    }

    public String generateNewSessionTimeTableId() {
        String lastId = sessionTimeTableRepo.findTopByOrderBySessionIdDesc()
                .map(SessionTimeTable::getSessionId)
                .orElse("SC000");

        int num = Integer.parseInt(lastId.substring(2)) + 1;

        return String.format("SC%03d", num);
    }

    public List<TodaySessionDTO> loadTodaySession(String licenseId, LocalDate todayDate) {
        List<SessionTimeTable> sessions = pendingSessionRepo.findByLicenseIdAndDate(licenseId, todayDate);

        if (sessions == null || sessions.isEmpty()) {
            return Collections.emptyList();
        }

        return sessions.stream()
                .map(st -> {
                    var ps = st.getPendingSession();

                    return new TodaySessionDTO(
                            st.getSessionId(),
                            ps.getSessionId(),
                            ps.getTime(),
                            ps.getDate(),
                            ps.getInstructor().getLicenseId(),
                            ps.getVehicleNumber(),
                            ps.getRespond() != null ? ps.getRespond().name() : null,
                            ps.getCourseName()
                    );
                })
                .collect(Collectors.toList());
    }

}
