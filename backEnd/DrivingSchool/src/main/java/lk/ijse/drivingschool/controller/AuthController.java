package lk.ijse.drivingschool.controller;

import jakarta.validation.Valid;
import lk.ijse.drivingschool.dto.*;
import lk.ijse.drivingschool.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/student/login")
    public ResponseEntity<ApiResponseDTO>login(@RequestBody StudentAuthDTO studentAuthDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.authenticate(studentAuthDTO)
        ));
    }

    @PostMapping("/employee/login")
    public ResponseEntity<ApiResponseDTO>employeeLogin(@RequestBody @Valid EmployeeAuthDTO employeeAuthDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.employeeAuthenticate(employeeAuthDTO)
        ));
    }


    @PostMapping("/student/signup")
    public ResponseEntity<ApiResponseDTO>registerStudent(@RequestBody @Valid StudentAuthDTO studentAuthDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.registerStudent(studentAuthDTO)
        ));
    }


    @PostMapping("/employee/signup")
    public ResponseEntity<ApiResponseDTO>employeeSignup(@RequestBody @Valid InstructorAuthDTO instructorAuthDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.employeeSignup(instructorAuthDTO)
        ));
    }

    @PostMapping("/employee/register")
    public ResponseEntity<ApiResponseDTO>registerEmployee(@RequestBody @Valid InstructorAuthDTO instructorAuthDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.admin(instructorAuthDTO)
        ));
    }
}
