package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.EmployeeDTO;
import lk.ijse.drivingschool.entity.Employee;
import lk.ijse.drivingschool.entity.enums.JobRole;
import lk.ijse.drivingschool.repository.EmployeeRepo;
import lk.ijse.drivingschool.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepository;

    public String registerEmployee(EmployeeDTO employeeDTO) {

        System.out.println(employeeDTO);

        if (employeeRepository.existsById(employeeDTO.getNic())) {
            throw new RuntimeException("Employee NIC already exists");
        }

        Employee employee = new Employee(
                employeeDTO.getNic(),
                employeeDTO.getName(),
                employeeDTO.getEmail(),
                employeeDTO.getAddress(),
                employeeDTO.getContact(),
                JobRole.INSTRUCTOR
        );
        employeeRepository.save(employee);
        return "Employee registered successfully";
    }

    public Object getAllInstructors() {

        List<Employee> employees = employeeRepository.findAllInstructorEmployees();

        return employees.stream()
                .map(employee -> new EmployeeDTO(
                        employee.getNic(),
                        employee.getAddress(),
                        employee.getContact(),
                        employee.getGmail(),
                        employee.getName()
                )).toList();
    }

    public EmployeeDTO getLoggedInstructorInfo(String nic) {

        Employee employee = employeeRepository.findInstructorByNic(nic)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return new EmployeeDTO(
                employee.getNic(),
                employee.getAddress(),
                employee.getContact(),
                employee.getGmail(),
                employee.getName()
        );
    }
    public String updateEmployee(String employeeId, EmployeeDTO employeeDTO) {

        Employee employee =employeeRepository.findById(employeeId).orElseThrow(()->new RuntimeException("Employee not found!!!!!!"));

        employee.setNic(employeeDTO.getNic());
        employee.setName(employeeDTO.getName());
        employee.setGmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());
        employee.setContact(employeeDTO.getContact());
        employee.setJobRole(JobRole.INSTRUCTOR);

        employeeRepository.save(employee);

        return "Employee updated successfully";
    }



    public String deleteEmployee(String employeeId) {
        employeeRepository.deleteById(employeeId);

        return "Employee deleted successfully";
    }

    public String updateProfile(String loggedNic, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findByNic(loggedNic).orElseThrow(()->new RuntimeException("Employee not found!!!!!!"));

        employee.setName(employeeDTO.getName());
        employee.setGmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());
        employee.setContact(employeeDTO.getContact());

        employeeRepository.save(employee);
        return "Employee updated successfully";
    }
}
