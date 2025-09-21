package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.entity.StudentSession;
import lk.ijse.drivingschool.entity.StudentSessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSessionRepo extends JpaRepository<StudentSession, StudentSessionId> {

    @Query("""
    SELECT new lk.ijse.drivingschool.dto.UpcomingSessionDTO(
        ps.sessionId,
        ps.courseName,
        ps.date,
        '',  \s
        ps.time,
        '', \s
        ''  \s
    )
    FROM StudentSession ss
    JOIN ss.session stt
    JOIN stt.pendingSession ps
    WHERE ss.student.nic = :nic
      AND (
          ps.date > CURRENT_DATE
          OR (ps.date = CURRENT_DATE AND ps.time > CURRENT_TIME)
      )
""")
    List<UpcomingSessionDTO> findUpcomingSessionsByNic(@Param("nic") String nic);


}
