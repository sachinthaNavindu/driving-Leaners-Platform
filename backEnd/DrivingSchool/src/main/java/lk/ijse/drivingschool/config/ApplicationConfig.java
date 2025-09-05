package lk.ijse.drivingschool.config;

import lk.ijse.drivingschool.entity.Employee;
import lk.ijse.drivingschool.entity.Instructor;
import lk.ijse.drivingschool.entity.Student;
import lk.ijse.drivingschool.entity.StudentCredentials;
import lk.ijse.drivingschool.repository.EmployeeRepo;
import lk.ijse.drivingschool.repository.InstructorRepository;
import lk.ijse.drivingschool.repository.StudentCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final StudentCredentialRepository studentCredentialRepository;
    private final EmployeeRepo employeeRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return nic -> {
            StudentCredentials studentCredentials = studentCredentialRepository.findByStudentNic(nic)
                    .orElse(null);
            if (studentCredentials != null) {
                return new org.springframework.security.core.userdetails.User(
                        studentCredentials.getStudentNic(),
                        studentCredentials.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                );
            }

            Employee employee = employeeRepository.findById(nic).orElse(null);
            if (employee != null) {
                return new org.springframework.security.core.userdetails.User(
                        employee.getNic(),
                        "Default_Password",
                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                );
            }

//            Instructor instructor = instructorRepository.findById(nic).orElse(null);
//            if (instructor != null) {
//                return new org.springframework.security.core.userdetails.User(
//                        instructor.getNic(),
//                        instructor.getPassword(),
//                        List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
//                );
//            }

            throw new UsernameNotFoundException("User not found with NIC: " + nic);
        };
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}