package lk.ijse.drivingschool.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        UpcomingSessionDTO session = sessionTimeTableService.getUpcomingSessions(userCourse);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("student", studentData);
        responseData.put("upcomingSession", session);

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
}
