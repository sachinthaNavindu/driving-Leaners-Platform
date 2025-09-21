package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.FetchCustomerSessionsDTO;
import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.dto.SessionTimeTableDTO;
import lk.ijse.drivingschool.dto.TodaySessionDTO;

import java.time.LocalDate;
import java.util.List;

public interface PendingSessionService {
    Object generateSessionId();
    String savePendingSession(SessionTimeTableDTO sessionTimeTableDTO);
    List<PendingSessionDTO> getAllSessions();
    List<PendingSessionDTO> getAllSessionsForUser(String licenseId);
    String manageRequest(String action, String nic, String sessionId);
    String generateNewSessionTimeTableId();
    List<TodaySessionDTO> loadTodaySession(String licenseId, LocalDate todayDate);
    String cancelSession(String sessionId);

    List<FetchCustomerSessionsDTO> getAllUpcomingSessions();
}
