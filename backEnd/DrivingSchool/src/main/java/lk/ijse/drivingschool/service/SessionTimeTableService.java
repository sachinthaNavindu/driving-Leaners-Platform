package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.InstructorScheduleDTO;
import lk.ijse.drivingschool.dto.PendingSessionDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;

import java.util.List;

public interface SessionTimeTableService {
    List<InstructorScheduleDTO> getScheduleByInstructor(String licenseId);
    List<UpcomingSessionDTO> getUpcomingSessions(String userCourse,String nic);
    long getCountTodaysSession();
    List<PendingSessionDTO> getAllSessionsFromToday();

}


