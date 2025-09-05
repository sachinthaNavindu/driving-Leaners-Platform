package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.entity.Student;
import lk.ijse.drivingschool.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public String registerStudent(StudentDTO studentDTO) {

        if (studentRepository.existsById(studentDTO.getNic())) {
            throw new RuntimeException("Student already exists");
        }

        Student student = new Student(
                studentDTO.getNic(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getAddress(),
                studentDTO.getContact()
        );

        studentRepository.save(student);
        return "Student registered successfully";

    }

    public Object getRegisteredStudents() {

        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(student -> new StudentDTO(
                        student.getNic(),
                        student.getName(),
                        student.getEmail(),
                        student.getAddress(),
                        student.getContact()
                )).toList();
    }
}
