package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.CourseDTO;

public interface CourseService {
    String saveCourse(CourseDTO courseDTO);
    Object getCourseDetails();
    String deleteCourse(String courseName);
    String updateCourse(CourseDTO courseDTO,String updateCourseName);
    int getCourseId(String courseName);
}
