package lk.ijse.drivingschool.controller;

import lk.ijse.drivingschool.dto.*;
import lk.ijse.drivingschool.service.CourseService;
import lk.ijse.drivingschool.service.EmployeeService;
import lk.ijse.drivingschool.service.StudentService;
import lk.ijse.drivingschool.service.VehicleService;
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
    private final VehicleService vehicleService;

    //register endpoints
    @RequestMapping("/studentRegister")
    public ResponseEntity<ApiResponseDTO>RegisterStudent(@RequestBody WrapperDTO wrapperDTO) {

        StudentDTO studentDTO = wrapperDTO.getStudentDTO();
        PaymentDTO paymentDTO = wrapperDTO.getPaymentDTO();

        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.registerStudent(studentDTO,paymentDTO)
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

    @RequestMapping("/vehicleSave")
    public ResponseEntity<ApiResponseDTO>saveVehicle(@RequestBody VehicleDTO vehicleDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleService.saveVehicle(vehicleDTO)
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

    @RequestMapping("/getVehicles")
    public ResponseEntity<ApiResponseDTO>GetVehicles(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleService.getVehicleData()
        ));
    }

    //update endpoints

    @RequestMapping("/studentUpdate")
    public ResponseEntity<ApiResponseDTO>StudentUpdate(@RequestBody StudentDTO studentDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.updateStudent(studentDTO)
        ));
    }

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

    @RequestMapping("/updateVehicle/{vehicleNumber}")
    public ResponseEntity<ApiResponseDTO>updateVehicle(@PathVariable String vehicleNumber, @RequestBody VehicleDTO vehicleDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleService.updateVehicle(vehicleNumber,vehicleDTO)
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
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.deleteCourse(courseToDeleteName)
        ));
    }

    @RequestMapping("/studentDelete/{nic}")
    public ResponseEntity<ApiResponseDTO>studentDelete(@PathVariable String nic){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.deleteStudent(nic)
        ));
    }

    @RequestMapping("/vehicleDelete/{vehicleNum}")
    public ResponseEntity<ApiResponseDTO>deleteVehicle(@PathVariable String vehicleNum){
        System.out.println(vehicleNum);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleService.deleteVehicle(vehicleNum)
        ));
    }

    @RequestMapping("/getStudentDetails")
    public ResponseEntity<ApiResponseDTO>getStudentDetails(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentService.getStudentCount()
        ));
    }
}
