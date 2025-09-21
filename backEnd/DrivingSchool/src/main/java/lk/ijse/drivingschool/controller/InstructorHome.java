    package lk.ijse.drivingschool.controller;

    import jakarta.validation.Valid;
    import lk.ijse.drivingschool.dto.ApiResponseDTO;
    import lk.ijse.drivingschool.dto.EmployeeDTO;
    import lk.ijse.drivingschool.service.EmployeeService;
    import lk.ijse.drivingschool.service.PendingSessionService;
    import lk.ijse.drivingschool.service.SessionTimeTableService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;

    @RestController
    @RequestMapping("/instructor")
    @RequiredArgsConstructor
    public class InstructorHome {

        private final EmployeeService employeeService;
        private final PendingSessionService pendingSessionService;
        private final SessionTimeTableService sessionTimeTableService;

        @RequestMapping("/loadDetails/{nic}")
        public ResponseEntity<ApiResponseDTO> loadLoginUserDetails(@PathVariable String nic){
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    employeeService.getLoggedInstructorInfo(nic)
            ));
        }

        @RequestMapping("/sessionLoad/{licenseId}")
        public ResponseEntity<ApiResponseDTO> pendingSessionRq(@PathVariable String licenseId){
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    pendingSessionService.getAllSessionsForUser(licenseId)
            ));
        }

        @RequestMapping("/sessionManagement/{action},{nic},{sessionId}")
        public ResponseEntity<ApiResponseDTO>ManageRequests(@PathVariable String action, @PathVariable String nic, @PathVariable String sessionId) {
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    pendingSessionService.manageRequest(action, nic, sessionId)
            ));
        }

        @RequestMapping("/loadSchedule/{licenseId}")
        public ResponseEntity<ApiResponseDTO>loadSchedule(@PathVariable String licenseId){
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    sessionTimeTableService.getScheduleByInstructor(licenseId)
            ));
        }

        @RequestMapping("/updateProfile/{nic}")
        public ResponseEntity<ApiResponseDTO> updateProfile(@PathVariable String nic,@RequestBody @Valid EmployeeDTO employeeDTO){
            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    employeeService.updateProfile(nic, employeeDTO)
            ));
        }

        @RequestMapping("/loadUpcomingSession/{licenseId}")
        public ResponseEntity<ApiResponseDTO> loadUpcoming(@PathVariable String licenseId){

            LocalDate today = LocalDate.now();

            return ResponseEntity.ok(new ApiResponseDTO(
                    200,
                    "OK",
                    pendingSessionService.loadTodaySession(licenseId,today)
            ));
        }
    }
