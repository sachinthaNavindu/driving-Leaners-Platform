package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.AuthResponseDTO;
import lk.ijse.drivingschool.dto.EmployeeAuthDTO;
import lk.ijse.drivingschool.dto.InstructorAuthDTO;
import lk.ijse.drivingschool.dto.StudentAuthDTO;

public interface AuthService {
    AuthResponseDTO authenticate(StudentAuthDTO studentAuthDTO);
    AuthResponseDTO employeeAuthenticate(EmployeeAuthDTO employeeAuthDTO);
    String registerStudent(StudentAuthDTO studentAuthDTO);
    String employeeSignup(InstructorAuthDTO instructorAuthDTO);
    Object admin(InstructorAuthDTO instructorAuthDTO);
}
