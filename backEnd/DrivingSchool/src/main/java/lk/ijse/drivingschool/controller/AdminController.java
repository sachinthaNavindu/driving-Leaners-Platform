package lk.ijse.drivingschool.controller;

import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.dto.EmployeeDTO;
import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.service.EmployeeService;
import lk.ijse.drivingschool.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentService studentService;
    private final EmployeeService employeeService;

    //register endpoints
    @RequestMapping("/studentRegister")
    public ResponseEntity<ApiResponseDTO>RegisterStudent(@RequestBody StudentDTO studentDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.registerStudent(studentDTO)
        ));
    }

    @RequestMapping("/employeeRegister")
    public ResponseEntity<ApiResponseDTO>registerEmployee(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("working here");
        return ResponseEntity.ok(new ApiResponseDTO(
           200,
           "OK",
            employeeService.registerEmployee(employeeDTO)
        ));
    }

    // get endpoints
    @RequestMapping("/getRegisteredStudents")
    public ResponseEntity<ApiResponseDTO>GetRegisteredStudents(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.getRegisteredStudents()
        ));
    }

    @RequestMapping("/getInstructors")
    public ResponseEntity<ApiResponseDTO>GetInstructors(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeService.getAllInstructors()
        ));
    }

    //update endpoints

    @RequestMapping("/employeeUpdate/{employeeId}")
    public ResponseEntity<ApiResponseDTO>employeeUpdate(@PathVariable String employeeId, @RequestBody EmployeeDTO employeeDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeService.updateEmployee(employeeId,employeeDTO)
        ));
    }

    //delete endpoints
    @RequestMapping("/employeeDelete/{employeeId}")
    public ResponseEntity<ApiResponseDTO>employeeDelete(@PathVariable String employeeId){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeService.deleteEmployee(employeeId)
        ));
    }
}
