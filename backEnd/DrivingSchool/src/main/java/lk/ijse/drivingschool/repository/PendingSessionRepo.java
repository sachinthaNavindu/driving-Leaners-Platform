package lk.ijse.drivingschool.repository;


import lk.ijse.drivingschool.entity.InstructorRespond;
import lk.ijse.drivingschool.entity.PendingSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PendingSessionRepo extends JpaRepository<PendingSessions,String> {
    @Query("SELECT  sessionId FROM PendingSessions ORDER BY  sessionId DESC limit 1")
    String getLastSessionId();

    long countAllByRespond(InstructorRespond respond);
    Optional<PendingSessions> findBySessionId(String sessionId);
}
