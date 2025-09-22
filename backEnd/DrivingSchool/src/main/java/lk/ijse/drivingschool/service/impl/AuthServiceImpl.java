package lk.ijse.drivingschool.service.impl;


import lk.ijse.drivingschool.dto.*;
import lk.ijse.drivingschool.entity.*;
import lk.ijse.drivingschool.entity.enums.InstructorStatus;
import lk.ijse.drivingschool.entity.enums.JobRole;
import lk.ijse.drivingschool.repository.*;
import lk.ijse.drivingschool.service.AuthService;
import lk.ijse.drivingschool.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final StudentCredentialRepository studentCredentialRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepo employeeRepo;
    private final AdminRepo adminRepo;
    private final InstructorRepository instructorRepository;


    //auths

    public AuthResponseDTO authenticate(StudentAuthDTO studentAuthDTO) {

        StudentCredentials studentCredentials = studentCredentialRepository.findByStudentNic(studentAuthDTO.getNic()).orElseThrow(
                () -> new RuntimeException("User not found"));

        JobRole role = JobRole.STUDENT;
        String name = studentAuthDTO.getUsername();

        if (!passwordEncoder.matches(studentAuthDTO.getPassword(), studentCredentials.getPassword())) {
            throw new BadCredentialsException("Invalid Nic or password");
        }
        String accessToken = jwtUtil.generateToken(studentAuthDTO.getNic());
        String refreshToken = jwtUtil.generateRefreshToken(studentAuthDTO.getNic());

        return new AuthResponseDTO(accessToken,refreshToken,studentCredentials.getStudentNic(),name,role,"");
    }

    public AuthResponseDTO employeeAuthenticate(EmployeeAuthDTO employeeAuthDTO) {

        Employee employee = employeeRepo.findByNicAndGmail(employeeAuthDTO.getNic(),employeeAuthDTO.getEmail()).orElseThrow(()->new RuntimeException("Employee not found"));

        JobRole jobrole = employee.getJobRole();
        String userName = employee.getName();
        String licenseId = "";

        switch (jobrole){
            case ADMIN:

                Admin admin = adminRepo.findByNic(employeeAuthDTO.getNic()).orElseThrow(()->new RuntimeException("Admin not found"));

                if (!passwordEncoder.matches(employeeAuthDTO.getPassword(), admin.getPassword())) {
                    throw new BadCredentialsException("Invalid Nic or password");
            }
            break;

            case INSTRUCTOR:

                Instructor instructor = instructorRepository.findByEmployee_Nic(employeeAuthDTO.getNic()).orElseThrow(()->new RuntimeException("Instructor not found"));
                licenseId = instructor.getLicenseId();
                if (!passwordEncoder.matches(employeeAuthDTO.getPassword(), instructor.getPassword())) {
                    throw new BadCredentialsException("Invalid Nic or password");
                }
                break;
                default:
                    throw new BadCredentialsException("Unsupported job role");
        }
        String accessToken = jwtUtil.generateToken(employeeAuthDTO.getNic());
        String refreshToken = jwtUtil.generateRefreshToken(employeeAuthDTO.getNic());

        return new AuthResponseDTO(accessToken,refreshToken,employeeAuthDTO.getNic(),userName,jobrole,licenseId);
    }


    // registrations

    public String registerStudent(StudentAuthDTO studentAuthDTO) {
        System.out.println(studentAuthDTO.getNic());

        Student student = studentRepository.findByNic(studentAuthDTO.getNic())
                .orElseThrow(() -> new RuntimeException("Student not found. Please register student first."));

        if (studentCredentialRepository.findByStudentNic(studentAuthDTO.getNic()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        StudentCredentials studentCredentials = StudentCredentials.builder()
                .studentNic(studentAuthDTO.getNic())
                .username(studentAuthDTO.getUsername())
                .password(passwordEncoder.encode(studentAuthDTO.getPassword()))
                .student(student)
                .build();

        studentCredentialRepository.save(studentCredentials);

        return "Student Signup successfully";
    }


    public String employeeSignup(InstructorAuthDTO instructorAuthDTO) {

        System.out.println(instructorAuthDTO);


        Employee employee = employeeRepo.findByNicAndGmail(instructorAuthDTO.getNic(),instructorAuthDTO.getEmail()).orElseThrow(() -> new RuntimeException("Employee not found") );

        System.out.println("working");

        if (instructorRepository.existsByNicOrLicenseId(instructorAuthDTO.getNic(),instructorAuthDTO.getLicenseId())) {
            throw new RuntimeException("Instructor already Exists");
        }

        Instructor instructor1 = Instructor.builder()
                .licenseId(instructorAuthDTO.getLicenseId())
                .nic(instructorAuthDTO.getNic())
                .status(InstructorStatus.ACTIVE)
                .password(passwordEncoder.encode(instructorAuthDTO.getPassword()))
        .build();

        instructorRepository.save(instructor1);
        return "Instructor Signup Successfully";
    }

    public Object admin(InstructorAuthDTO instructorAuthDTO) {
        Admin admin = Admin.builder()
                .nic(instructorAuthDTO.getNic())
                .password(passwordEncoder.encode(instructorAuthDTO.getPassword()))
                .build();

        return adminRepo.save(admin);
    }

    public Map<String, String> refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(username);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", refreshToken
        );
    }
}
