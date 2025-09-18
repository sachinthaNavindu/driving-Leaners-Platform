package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.CustomStudentDTO;
import lk.ijse.drivingschool.dto.PaymentDTO;
import lk.ijse.drivingschool.dto.StudentDTO;

public interface StudentService {
    String registerStudent(StudentDTO studentDTO, PaymentDTO paymentDTO);
    Object getRegisteredStudents();
    String updateStudent(StudentDTO studentDTO);
    boolean deleteStudent(String nic);
    CustomStudentDTO getStudentCount();
    StudentDTO loggedStudentData(String nic);
}
