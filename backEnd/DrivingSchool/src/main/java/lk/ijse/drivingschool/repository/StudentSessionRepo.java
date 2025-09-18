package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.StudentSession;
import lk.ijse.drivingschool.entity.StudentSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSessionRepo extends JpaRepository<StudentSession, StudentSessionId> {
}
