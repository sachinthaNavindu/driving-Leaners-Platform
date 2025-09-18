package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.InstructorDTO;
import lk.ijse.drivingschool.repository.EmployeeRepo;
import lk.ijse.drivingschool.repository.InstructorRepository;
import lk.ijse.drivingschool.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

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
