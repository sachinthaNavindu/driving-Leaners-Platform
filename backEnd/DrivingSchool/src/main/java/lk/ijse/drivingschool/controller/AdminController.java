package lk.ijse.drivingschool.controller;

import jakarta.validation.Valid;
import lk.ijse.drivingschool.dto.*;
import lk.ijse.drivingschool.service.*;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentService studentServiceImpl;
    private final EmployeeService employeeServiceImpl;
    private final CourseService courseServiceImpl;
    private final VehicleService vehicleServiceImpl;
    private final SessionTimeTableService sessionTimeTableServiceImpl;
    private final PendingSessionService pendingSessionServiceImpl;
    private final ApplicationService applicationServiceImpl;

    @RequestMapping("/studentRegister")
    public ResponseEntity<ApiResponseDTO>RegisterStudent(@RequestBody @Valid WrapperDTO wrapperDTO) {
        StudentDTO studentDTO = wrapperDTO.getStudentDTO();
        PaymentDTO paymentDTO = wrapperDTO.getPaymentDTO();

        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentServiceImpl.registerStudent(studentDTO,paymentDTO)
        ));
    }

    @RequestMapping("/employeeRegister")
    public ResponseEntity<ApiResponseDTO>registerEmployee(@RequestBody @Valid EmployeeDTO employeeDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
           200,
           "OK",
            employeeServiceImpl.registerEmployee(employeeDTO)
        ));
    }

    @RequestMapping("/saveCourse")
    public ResponseEntity<ApiResponseDTO>saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        System.out.println(courseDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseServiceImpl.saveCourse(courseDTO)
        ));
    }

    @RequestMapping("/vehicleSave")
    public ResponseEntity<ApiResponseDTO>saveVehicle(@RequestBody @Valid VehicleDTO vehicleDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleServiceImpl.saveVehicle(vehicleDTO)
        ));
    }

    // get endpoints
    @RequestMapping("/getCourseDetails")
    public ResponseEntity<ApiResponseDTO>getCourseDetails(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseServiceImpl.getCourseDetails()
        ));
    }

    @RequestMapping("/getRegisteredStudents")
    public ResponseEntity<ApiResponseDTO>GetRegisteredStudents(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentServiceImpl.getRegisteredStudents()
        ));
    }

    @RequestMapping("/getInstructors")
    public ResponseEntity<ApiResponseDTO>GetInstructors(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeServiceImpl.getAllInstructors()
        ));
    }

    @RequestMapping("/getVehicles")
    public ResponseEntity<ApiResponseDTO>GetVehicles(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleServiceImpl.getVehicleData()
        ));
    }

    //update endpoints

    @RequestMapping("/studentUpdate")
    public ResponseEntity<ApiResponseDTO>StudentUpdate(@RequestBody @Valid StudentDTO studentDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentServiceImpl.updateStudent(studentDTO)
        ));
    }

    @RequestMapping("/employeeUpdate/{employeeId}")
    public ResponseEntity<ApiResponseDTO>employeeUpdate(@PathVariable String employeeId, @RequestBody @Valid EmployeeDTO employeeDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeServiceImpl.updateEmployee(employeeId,employeeDTO)
        ));
    }

    @RequestMapping("/courseUpdate/{currentEditCourseName}")
    public ResponseEntity<ApiResponseDTO>courseUpdate(@PathVariable String currentEditCourseName, @RequestBody @Valid CourseDTO courseDTO){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseServiceImpl.updateCourse(courseDTO,currentEditCourseName)
        ));
    }

    @RequestMapping("/updateVehicle/{vehicleNumber}")
    public ResponseEntity<ApiResponseDTO>updateVehicle(@PathVariable String vehicleNumber, @RequestBody @Valid VehicleDTO vehicleDTO){
        System.out.println(vehicleDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleServiceImpl.updateVehicle(vehicleNumber,vehicleDTO)
        ));
    }

    //delete endpoints
    @RequestMapping("/employeeDelete/{employeeId}")
    public ResponseEntity<ApiResponseDTO>employeeDelete(@PathVariable String employeeId){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                employeeServiceImpl.deleteEmployee(employeeId)
        ));
    }

    @RequestMapping("/courseDelete/{courseToDeleteName}")
    public ResponseEntity<ApiResponseDTO>courseDelete(@PathVariable String courseToDeleteName){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseServiceImpl.deleteCourse(courseToDeleteName)
        ));
    }

    @RequestMapping("/studentDelete/{nic}")
    public ResponseEntity<ApiResponseDTO>studentDelete(@PathVariable String nic){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentServiceImpl.deleteStudent(nic)
        ));
    }

    @RequestMapping("/vehicleDelete/{vehicleNum}")
    public ResponseEntity<ApiResponseDTO>deleteVehicle(@PathVariable String vehicleNum){
        System.out.println(vehicleNum);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                vehicleServiceImpl.deleteVehicle(vehicleNum)
        ));
    }

    @RequestMapping("/getStudentDetails")
    public ResponseEntity<ApiResponseDTO>getStudentDetails(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                studentServiceImpl.getStudentCount()
        ));
    }

    @RequestMapping("/loadData")
    public ResponseEntity<ApiResponseDTO>loadData(){

       CustomStudentDTO customStudentDTO  = studentServiceImpl.getStudentCount();
       long courseCount = courseServiceImpl.getCourseCount();
       long sessionCount = sessionTimeTableServiceImpl.getCountTodaysSession();
       List<PendingSessionDTO> scheduleSessions = sessionTimeTableServiceImpl.getAllSessionsFromToday();
       List<FetchCustomerSessionsDTO> upcomingSessions = pendingSessionServiceImpl.getAllUpcomingSessions();


        Map<String, Object> responseData = new HashMap<>();
        responseData.put("studentCount", customStudentDTO);
        responseData.put("courseCount", courseCount);
        responseData.put("todaysSessionCount", sessionCount);
        responseData.put("scheduleSessions", scheduleSessions);
        responseData.put("upcomingSessions", upcomingSessions);


        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                responseData
        ));
    }

    @RequestMapping("/approve/{id}/{actionTaken}")
    public ResponseEntity<ApiResponseDTO>approve(@PathVariable Long id,@PathVariable String actionTaken ){
        System.out.println(id);
        System.out.println(actionTaken);
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                applicationServiceImpl.approve(id,actionTaken)
        ));
    }
}
