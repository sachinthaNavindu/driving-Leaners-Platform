package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.entity.PendingSessions;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionTimeTableRepo extends JpaRepository<SessionTimeTable,String> {

    Optional<SessionTimeTable> findTopByOrderBySessionIdDesc();

    @Query("SELECT st.sessionId, ps.time, ps.date, ps.vehicleNumber, ps.courseName FROM SessionTimeTable st JOIN st.pendingSession ps WHERE ps.instructor.licenseId = :licenseId")
    List<Object[]> findInstructorSchedule(@Param("licenseId") String licenseId);

    Optional<SessionTimeTable> findTopByPendingSession_CourseNameAndPendingSession_RespondAndPendingSession_DateGreaterThanEqualOrderByPendingSession_DateAsc(
            String courseName, InstructorRespond respond, LocalDate today);

    Optional<SessionTimeTable> findBySessionId(String sessionId);
}



