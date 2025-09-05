package lk.ijse.drivingschool.service;


import lk.ijse.drivingschool.dto.*;
import lk.ijse.drivingschool.entity.*;
import lk.ijse.drivingschool.repository.*;
import lk.ijse.drivingschool.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

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
        String token = jwtUtil.generateToken(studentAuthDTO.getNic());

        return new AuthResponseDTO(token,studentCredentials.getStudentNic(),name,role);
    }

    public AuthResponseDTO employeeAuthenticate(EmployeeAuthDTO employeeAuthDTO) {

        Employee employee = employeeRepo.findByNic(employeeAuthDTO.getNic()).orElseThrow(()->new RuntimeException("Employee not found"));

        JobRole jobrole = employee.getJobRole();
        String userName = employee.getName();

        switch (jobrole){
            case ADMIN:

                Admin admin = adminRepo.findByNic(employeeAuthDTO.getNic()).orElseThrow(()->new RuntimeException("Admin not found"));

                if (!passwordEncoder.matches(employeeAuthDTO.getPassword(), admin.getPassword())) {
                    throw new BadCredentialsException("Invalid Nic or password");
            }
            break;

            case INSTRUCTOR:

                Instructor instructor = instructorRepository.findByEmployee_Nic(employeeAuthDTO.getNic()).orElseThrow(()->new RuntimeException("Instructor not found"));

                if (!passwordEncoder.matches(employeeAuthDTO.getPassword(), instructor.getPassword())) {
                    throw new BadCredentialsException("Invalid Nic or password");
                }
                break;
                default:
                    throw new BadCredentialsException("Unsupported job role");
        }
        String token = jwtUtil.generateToken(employeeAuthDTO.getNic());
        return new AuthResponseDTO(token,employeeAuthDTO.getNic(),userName,jobrole);
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
                .password(passwordEncoder.encode(instructorAuthDTO.getPassword()))
        .build();

        instructorRepository.save(instructor1);
        return "Instructor Signup Successfully";
    }

}
