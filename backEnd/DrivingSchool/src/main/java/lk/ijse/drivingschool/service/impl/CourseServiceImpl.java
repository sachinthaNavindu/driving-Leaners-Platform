package lk.ijse.drivingschool.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.drivingschool.dto.CourseDTO;
import lk.ijse.drivingschool.entity.Course;
import lk.ijse.drivingschool.repository.CourseRepo;
import lk.ijse.drivingschool.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    public String saveCourse(CourseDTO courseDTO) {
        if (courseRepo.existsByCourseName(courseDTO.getCourseName())){
            throw new RuntimeException("Course is already exists by this course name");
        }

        Course course =  Course.builder()
                .courseName(courseDTO.getCourseName())
                .courseFee(courseDTO.getCourseFee())
                .sessions(courseDTO.getSessions())
                .build();
        courseRepo.save(course);
        return "Course registered successfully";

    }

    public Object getCourseDetails() {
        List<Course> courses = courseRepo.findAll();
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getCourseName(),
                        course.getCourseFee(),
                        course.getSessions()
                )).toList();
    }

    @Transactional
    public String deleteCourse(String courseName) {

        courseRepo.deleteByCourseName(courseName);

        return "Course deleted successfully";
    }

    public String updateCourse(CourseDTO courseDTO,String updateCourseName) {

        Course course =courseRepo.findByCourseName(updateCourseName).orElseThrow(()->new RuntimeException("Course doesn't exists By this Name"));

        course.setCourseName(courseDTO.getCourseName());
        course.setSessions(courseDTO.getSessions());
        course.setCourseFee(courseDTO.getCourseFee());
        courseRepo.save(course);

        return "Course updated successfully";
    }

    public int getCourseId(String courseName) {
        Optional<Course> courseOpt = courseRepo.findByCourseName(courseName);
        if (courseOpt.isPresent()) {
            return courseOpt.get().getCourseId();
        } else {
            throw new RuntimeException("Course not found: " + courseName);
        }
    }
}
