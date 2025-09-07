package lk.ijse.drivingschool.repository;

import lk.ijse.drivingschool.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Integer> {
    boolean existsByCourseName(String courseName);
    void deleteByCourseName(String courseName);
    Optional<Course> findByCourseName(String courseName);
}
