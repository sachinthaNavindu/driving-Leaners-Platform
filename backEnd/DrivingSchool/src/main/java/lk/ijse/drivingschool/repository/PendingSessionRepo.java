package lk.ijse.drivingschool.repository;


import lk.ijse.drivingschool.dto.TodaySessionDTO;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import lk.ijse.drivingschool.entity.PendingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PendingSessionRepo extends JpaRepository<PendingSessions,String> {
    @Query("SELECT  sessionId FROM PendingSessions ORDER BY  sessionId DESC limit 1")
    String getLastSessionId();

    long countAllByRespond(InstructorRespond respond);
    Optional<PendingSessions> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    List<PendingSessions> findByInstructor_LicenseIdAndRespond(String licenseId,InstructorRespond respond);

    @Query("SELECT st FROM SessionTimeTable st JOIN st.pendingSession ps WHERE ps.respond ='ACCEPTED' AND ps.date = :date AND ps.instructor.licenseId = :licenseId")
    List<SessionTimeTable> findByLicenseIdAndDate(@Param("licenseId") String licenseId,@Param("date") LocalDate date);
}
