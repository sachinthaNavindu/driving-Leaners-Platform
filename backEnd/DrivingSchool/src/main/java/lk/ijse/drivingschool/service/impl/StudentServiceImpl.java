package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.CustomStudentDTO;
import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.dto.StudentDTO;
import lk.ijse.drivingschool.entity.Course;
import lk.ijse.drivingschool.entity.Payment;
import lk.ijse.drivingschool.entity.Student;
import lk.ijse.drivingschool.repository.CourseRepo;
import lk.ijse.drivingschool.repository.PaymentRepo;
import lk.ijse.drivingschool.repository.StudentCredentialRepository;
import lk.ijse.drivingschool.repository.StudentRepository;
import lk.ijse.drivingschool.service.StudentService;
import lk.ijse.drivingschool.util.SendSlipEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PaymentRepo paymentRepo;
    private final CourseRepo courseRepo;
    private final StudentCredentialRepository studentCredentialRepository;

    @Transactional
    public String registerStudent(StudentDTO studentDTO, PaymentDTO paymentDTO) {
        Optional<Course> courseOpt = courseRepo.findByCourseName(paymentDTO.getCourseName());

        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found: " + paymentDTO.getCourseName());
        }

        Course course = courseOpt.get();

        Student student = Student.builder()
                .nic(studentDTO.getNic())
                .name(studentDTO.getName())
                .email(studentDTO.getEmail())
                .address(studentDTO.getAddress())
                .contact(studentDTO.getContact())
                .build();
        studentRepository.save(student);

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .paymentAmount(paymentDTO.getPaidAmount())
                .paymentDate(Timestamp.valueOf(paymentDTO.getPaymentDate() + " 00:00:00"))
                .paymentTime(Timestamp.valueOf("1970-01-01 " + paymentDTO.getPaidTime() + ":00"))
                .student(student)
                .course(course)
                .build();

        paymentRepo.save(payment);

        try {
            SendSlipEmail.sendPaymentSlipEmail(student.getEmail(), PaymentDTO.builder()
                    .paymentID(payment.getPaymentId())
                    .studentNic(student.getNic())
                    .courseName(course.getCourseName())
                    .paidAmount(payment.getPaymentAmount())
                    .paymentDate(paymentDTO.getPaymentDate())
                    .paidTime(paymentDTO.getPaidTime())
                    .build()
            );
            System.out.println("Payment slip email sent to " + student.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String updateStudent(StudentDTO studentDTO) {

        Student student = studentRepository.findByNic(studentDTO.getNic()).orElseThrow(()->new RuntimeException("Student not found!!!!!!"));

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setAddress(studentDTO.getAddress());
        student.setContact(studentDTO.getContact());
        studentRepository.save(student);

        return "Student updated successfully";
    }

    @Transactional
    public boolean deleteStudent(String nic) {
        int rowsDeleted = studentRepository.deleteByNic(nic);
        return rowsDeleted > 0;
    }

    public CustomStudentDTO getStudentCount() {
       long registeredStudentCount = studentRepository.count();
       long activeStudentCount = studentCredentialRepository.count();

       return new CustomStudentDTO(registeredStudentCount,activeStudentCount,0);
    }

    public StudentDTO loggedStudentData(String nic) {
        Student student = studentRepository.findByNic(nic).orElseThrow(()->new RuntimeException("Student not found!!!!!!"));

        return new StudentDTO(
                student.getNic(),
                student.getName(),
                student.getEmail(),
                student.getContact(),
                student.getAddress()
        );

    }

}
