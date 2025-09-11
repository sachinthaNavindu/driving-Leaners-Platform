package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.SessionTimeTableDTO;
import lk.ijse.drivingschool.entity.InstructorRespond;
import lk.ijse.drivingschool.entity.PendingSessions;
import lk.ijse.drivingschool.repository.PendingSessionRepo;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lk.ijse.drivingschool.util.EmailBodyUtil;
import lk.ijse.drivingschool.util.SendMailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PendingSessionService {
    private final PendingSessionRepo pendingSessionRepo;
    private final SessionTimeTableRepo sessionTimeTableRepo;

    public String savePendingSession(SessionTimeTableDTO sessionTimeTableDTO ,String instructorEmail) {

        if (sessionTimeTableRepo.existsById(sessionTimeTableDTO.getSessionId()) || pendingSessionRepo.existsById(sessionTimeTableDTO.getSessionId())) {
            throw new RuntimeException("Session already exists");
        }


        PendingSessions pendingSessions = new PendingSessions(
                sessionTimeTableDTO.getSessionId(),
                sessionTimeTableDTO.getTime(),
                sessionTimeTableDTO.getDate(),
                sessionTimeTableDTO.getInstructorName(),
                sessionTimeTableDTO.getNic(),
                sessionTimeTableDTO.getVehicleNumber(),
                InstructorRespond.PENDING,
                sessionTimeTableDTO.getCourseName()
        );
        pendingSessionRepo.save(pendingSessions);

        String emailBody =EmailBodyUtil.generateEmailBody("hello");
        SendMailUtil.sendEmailAsync(instructorEmail,"Booked Session Notification", emailBody);
        return "Pending session saved successfully";

    }

    public Object getAllSessions() {
        List<PendingSessions>allSessions = pendingSessionRepo.findAll();
        return  allSessions.stream()
                .map(pendingSessions -> new SessionTimeTableDTO(
                        pendingSessions.getSessionId(),
                        pendingSessions.getTime(),
                        pendingSessions.getDate(),
                        pendingSessions.getInstructorName(),
                        pendingSessions.getNic(),
                        pendingSessions.getVehicleNumber(),
                        pendingSessions.getRespond(),
                        pendingSessions.getCourseName()
                )).toList();
    }

    public String cancelSession(String sessionId) {

        PendingSessions pendingSessions = pendingSessionRepo.findBySessionId(sessionId).orElseThrow(()->new RuntimeException("Session not found"));

        pendingSessions.setRespond(InstructorRespond.CANCELED);
        pendingSessionRepo.save(pendingSessions);
        return "Session cancelled";
    }
}
