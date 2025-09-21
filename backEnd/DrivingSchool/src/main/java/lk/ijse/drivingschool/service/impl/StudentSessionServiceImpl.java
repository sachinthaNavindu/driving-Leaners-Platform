package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.entity.SessionTimeTable;
import lk.ijse.drivingschool.entity.Student;
import lk.ijse.drivingschool.entity.StudentSession;
import lk.ijse.drivingschool.entity.StudentSessionId;
import lk.ijse.drivingschool.repository.SessionTimeTableRepo;
import lk.ijse.drivingschool.repository.StudentRepository;
import lk.ijse.drivingschool.repository.StudentSessionRepo;
import lk.ijse.drivingschool.service.StudentSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentSessionServiceImpl implements StudentSessionService {

    private final StudentSessionRepo studentSessionRepo;
    private final StudentRepository studentRepository;
    private final SessionTimeTableRepo sessionTimeTableRepository;

    public String confirmParticipation(String sessionId, String nic) {

        Student student = studentRepository.findByNic(nic)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        SessionTimeTable session = sessionTimeTableRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));


        StudentSession studentSession = StudentSession.builder()
                .student(student)
                .session(session)
                .id(new StudentSessionId(student.getNic(), session.getSessionId()))
                .attendance(true)
                .build();

        studentSessionRepo.save(studentSession);

        return "OK";
    }

    @Override
    public List<UpcomingSessionDTO> getSessionsByStudent(String nic) {
        return studentSessionRepo.findUpcomingSessionsByNic(nic);
    }


}
