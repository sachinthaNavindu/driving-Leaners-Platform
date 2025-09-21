package lk.ijse.drivingschool.controller;

import jakarta.validation.Valid;
import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.dto.UpcomingSessionDTO;
import lk.ijse.drivingschool.service.PaymentService;
import lk.ijse.drivingschool.service.SessionTimeTableService;
import lk.ijse.drivingschool.service.StudentService;
import lk.ijse.drivingschool.service.StudentSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final SessionTimeTableService sessionTimeTableService;
    private final PaymentService paymentService;
    private final StudentSessionService studentSessionService;

    @RequestMapping("/loadData/{nic}")
    public ResponseEntity<ApiResponseDTO> loadData(@PathVariable String nic) {

        StudentDTO studentData = studentService.loggedStudentData(nic);
        String userCourse = paymentService.getUserCourse(nic);
        List<UpcomingSessionDTO> session =  sessionTimeTableService.getUpcomingSessions(userCourse,nic);
        List<UpcomingSessionDTO> confirmSessions =  studentSessionService.getSessionsByStudent(nic);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("student", studentData);
        responseData.put("upcomingSession", session);
        responseData.put("sessionsForUser", confirmSessions);

        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                responseData
        ));
    }

    @RequestMapping("/confirm/{sessionId}/{nic}")
    public ResponseEntity<ApiResponseDTO> confirm(@PathVariable String sessionId, @PathVariable String nic) {
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentSessionService.confirmParticipation(sessionId,nic)
        ));
    }

    @RequestMapping("/updateProfile/{nic}")
    public ResponseEntity<ApiResponseDTO> updateProfile(@PathVariable String nic ,@RequestBody @Valid StudentDTO studentDTO) {
        System.out.println(studentDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.update(studentDTO,nic)
        ));
    }
}
