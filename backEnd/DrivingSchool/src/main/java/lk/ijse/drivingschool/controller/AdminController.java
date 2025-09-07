package lk.ijse.drivingschool.controller;

import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.dto.CourseDTO;
import lk.ijse.drivingschool.dto.EmployeeDTO;
import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.service.CourseService;
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
    private final CourseService courseService;

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
        return ResponseEntity.ok(new ApiResponseDTO(
           200,
           "OK",
            employeeService.registerEmployee(employeeDTO)
        ));
    }

    @RequestMapping("/saveCourse")
    public ResponseEntity<ApiResponseDTO>saveCourse(@RequestBody CourseDTO courseDTO){
        System.out.println(courseDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.saveCourse(courseDTO)
        ));
    }

    // get endpoints
    @RequestMapping("/getCourseDetails")
    public ResponseEntity<ApiResponseDTO>getCourseDetails(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.getCourseDetails()
        ));
    }

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

    @RequestMapping("/courseUpdate/{currentEditCourseName}")
    public ResponseEntity<ApiResponseDTO>courseUpdate(@PathVariable String currentEditCourseName, @RequestBody CourseDTO courseDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.updateCourse(courseDTO,currentEditCourseName)
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

    @RequestMapping("/courseDelete/{courseToDeleteName}")
    public ResponseEntity<ApiResponseDTO>courseDelete(@PathVariable String courseToDeleteName){
        System.out.println(courseToDeleteName);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.deleteCourse(courseToDeleteName)
        ));
    }
}
