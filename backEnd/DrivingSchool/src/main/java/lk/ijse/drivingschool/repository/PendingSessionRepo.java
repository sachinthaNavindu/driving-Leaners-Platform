package lk.ijse.drivingschool.repository;


import lk.ijse.drivingschool.dto.FetchCustomerSessionsDTO;
import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lk.ijse.drivingschool.entity.PendingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PendingSessionRepo extends JpaRepository<PendingSessions,String> {
    @Query("SELECT  sessionId FROM PendingSessions ORDER BY  sessionId DESC limit 1")
    String getLastSessionId();

    long countAllByRespond(InstructorRespond respond);

    boolean existsBySessionId(String sessionId);

    List<PendingSessions> findByInstructor_LicenseIdAndRespond(String licenseId,InstructorRespond respond);

    @Query("SELECT st FROM SessionTimeTable st JOIN st.pendingSession ps WHERE ps.instructor.licenseId = :licenseId AND (ps.date = CURRENT_DATE OR (ps.date = CURRENT_DATE AND ps.time >= CURRENT_TIME)) ORDER BY ps.date ASC, ps.time ASC")
    List<SessionTimeTable> findNextSessionForInstructor(@Param("licenseId") String licenseId);

    List<PendingSessions> findByDateGreaterThanEqual(LocalDate date);

    @Query("SELECT new lk.ijse.drivingschool.dto.FetchCustomerSessionsDTO(" +
            "st.sessionId, ps.date, ps.time, ps.instructor.licenseId, ps.vehicle.vehicleNumber, ps.courseName) " +
            "FROM SessionTimeTable st " +
            "JOIN st.pendingSession ps " +
            "WHERE ps.respond = 'ACCEPTED' AND ps.date > CURRENT_DATE")
    List<FetchCustomerSessionsDTO> fetchAcceptedSessions();
}
