package lk.ijse.drivingschool.repository;


import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.enums.InstructorRespond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionTimeTableRepo extends JpaRepository<SessionTimeTable,String> {

    Optional<SessionTimeTable> findTopByOrderBySessionIdDesc();

    @Query("SELECT st.sessionId, ps.time, ps.date, ps.vehicle.vehicleNumber, ps.courseName FROM SessionTimeTable st JOIN st.pendingSession ps WHERE ps.instructor.licenseId = :licenseId")
    List<Object[]> findInstructorSchedule(@Param("licenseId") String licenseId);

    @Query("""
        SELECT st
        FROM SessionTimeTable st
        JOIN st.pendingSession ps
        LEFT JOIN st.studentSessions ss
            WITH ss.student.nic = :studentNic
        WHERE ps.courseName = :courseName
          AND ps.date >= :fromDate
          AND ss.id IS NULL
        ORDER BY ps.date ASC, ps.time ASC
    """)
    List<SessionTimeTable> findUpcomingSessionsForStudent(
            @Param("courseName") String courseName,
            @Param("studentNic") String studentNic,
            @Param("fromDate") LocalDate fromDate,
            Pageable pageable
    );

    Optional<SessionTimeTable> findBySessionId(String sessionId);

    @Query("SELECT COUNT(st) FROM SessionTimeTable st WHERE st.pendingSession.date = :today AND st.pendingSession.respond = lk.ijse.drivingschool.entity.enums.InstructorRespond.ACCEPTED")
    long countTodaysSessions(LocalDate today);


    @Query("SELECT st FROM SessionTimeTable st JOIN FETCH st.pendingSession ps JOIN FETCH ps.instructor i WHERE ps.date = :today ORDER BY ps.date ASC, ps.time ASC")
    List<SessionTimeTable> findApprovedSessionsFromToday(@Param("today") LocalDate today);


}



