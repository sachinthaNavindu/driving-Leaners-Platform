    package lk.ijse.drivingschool.controller;


    import lk.ijse.drivingschool.dto.ApiResponseDTO;
    import lk.ijse.drivingschool.dto.SessionTimeTableDTO;
    import lk.ijse.drivingschool.service.*;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/schedule")
    @RequiredArgsConstructor
    public class ScheduleManagerController {

        private final InstructorService instructorService;
        private final VehicleService vehicleService;
        private final SessionTimeTableService sessionTimeTableService;
        private final PendingSessionService pendingSessionService;
        private final CourseService courseService;

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

        @RequestMapping("/sessionId")
        public ResponseEntity<ApiResponseDTO> getSessionId(){
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    sessionTimeTableService.generateSessionId()
            ));
        }

        @RequestMapping("/saveSession")
        public ResponseEntity<ApiResponseDTO>saveSession(@RequestBody SessionTimeTableDTO sessionTimeTableDTO){
            String instructorEmail = instructorService.getInstructorEmail(sessionTimeTableDTO.getNic());

            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    pendingSessionService.savePendingSession(sessionTimeTableDTO,instructorEmail)
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
            System.out.println(sessionId);
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    pendingSessionService.cancelSession(sessionId)
            ));
        }
    }
