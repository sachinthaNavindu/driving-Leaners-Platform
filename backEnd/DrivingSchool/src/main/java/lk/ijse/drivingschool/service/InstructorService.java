package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.EmployeeDTO;
import lk.ijse.drivingschool.dto.InstructorDTO;
import lk.ijse.drivingschool.entity.Employee;
import lk.ijse.drivingschool.repository.EmployeeRepo;
import lk.ijse.drivingschool.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final EmployeeRepo employeeRepo;

    public Object getAvailableInstructors() {
        List<Object[]> availableInstructors = employeeRepo.findAllInstructorsByStatus();

        return availableInstructors.stream()
                .map(row -> new InstructorDTO(
                        (String) row[1],
                        (String) row[0]
                )).toList();
    }

    public String getInstructorEmail(String nic) {

        return employeeRepo.findGmailByLicenseId(nic);
    }
}
