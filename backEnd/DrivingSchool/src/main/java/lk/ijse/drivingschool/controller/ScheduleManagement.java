package lk.ijse.drivingschool.controller;

import jakarta.validation.Valid;
import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.dto.SessionTimeTableDTO;
import lk.ijse.drivingschool.service.CourseService;
import lk.ijse.drivingschool.service.InstructorService;
import lk.ijse.drivingschool.service.PendingSessionService;
import lk.ijse.drivingschool.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleManagement {

   private final PendingSessionService pendingSessionService;
   private final InstructorService instructorService;
   private final VehicleService vehicleService;
   private final CourseService courseService;

    @RequestMapping("/sessionId")
    public ResponseEntity<ApiResponseDTO> getSessionId(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                pendingSessionService.generateSessionId()
        ));
    }

    @RequestMapping("/getInstructor")
    public ResponseEntity<ApiResponseDTO> getInstructor(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                instructorService.getAvailableInstructors()
        ));
    }

    @RequestMapping("/getVehicle")
    public ResponseEntity<ApiResponseDTO> getVehicle(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleService.getAvailableVehicles()
        ));
    }

    @RequestMapping("/getCourses")
    public ResponseEntity<ApiResponseDTO> getCourses(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.getCourseDetails()
        ));
    }

    @RequestMapping("/saveSession")
    public ResponseEntity<ApiResponseDTO> saveSession(@RequestBody @Valid SessionTimeTableDTO sessionTimeTableDTO){

        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                pendingSessionService.savePendingSession(sessionTimeTableDTO)
        ));
    }

    @RequestMapping("/getSession")
    public ResponseEntity<ApiResponseDTO> getSession(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                pendingSessionService.getAllSessions()
        ));
    }

    @RequestMapping("/cancelSession/{sessionId}")
    public ResponseEntity<ApiResponseDTO> cancelSession(@PathVariable String sessionId){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                pendingSessionService.cancelSession(sessionId)
        ));
    }
}
