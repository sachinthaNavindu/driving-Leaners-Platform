package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.EmployeeDTO;

public interface EmployeeService {
    String registerEmployee(EmployeeDTO employeeDTO);
    Object getAllInstructors();
    EmployeeDTO getLoggedInstructorInfo(String nic);
    String updateEmployee(String employeeId, EmployeeDTO employeeDTO);
    String deleteEmployee(String employeeId);
    String updateProfile(String loggedNic, EmployeeDTO employeeDTO);

}
