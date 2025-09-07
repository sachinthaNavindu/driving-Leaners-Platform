package lk.ijse.drivingschool.controller;


import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.service.InstructorService;
import lk.ijse.drivingschool.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleManagerController {

    private final InstructorService instructorService;
    private final VehicleService vehicleService;

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
}
