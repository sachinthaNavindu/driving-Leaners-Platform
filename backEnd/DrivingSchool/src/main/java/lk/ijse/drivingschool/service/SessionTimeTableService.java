package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.InstructorScheduleDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;

import java.util.List;

public interface SessionTimeTableService {
    List<InstructorScheduleDTO> getScheduleByInstructor(String licenseId);
    UpcomingSessionDTO getUpcomingSessions(String userCourse);

}
