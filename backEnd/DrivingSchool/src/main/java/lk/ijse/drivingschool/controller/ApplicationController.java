package lk.ijse.drivingschool.controller;

import lk.ijse.drivingschool.dto.ResponseApplicationDTO;
import lk.ijse.drivingschool.service.CourseService;
import org.springframework.core.io.Resource;
import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.dto.StudentApplicationDTO;
import lk.ijse.drivingschool.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CourseService courseService;

    @RequestMapping("/apply")
    public ResponseEntity<ApiResponseDTO>apply(StudentApplicationDTO studentApplicationDTO) throws IOException {
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                applicationService.saveApplication(studentApplicationDTO)
                ));
    }
    @RequestMapping("/getApply")
    public ResponseEntity<ApiResponseDTO>getApply() throws IOException {
        List<ResponseApplicationDTO> applications = applicationService.getApplications();
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                applications
        ));
    }
    @GetMapping("/imageDB/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String folder,
            @PathVariable String filename) throws IOException {

        Path path = Paths.get("D:\\Ijse\\Aad\\finalProject\\frontEnd\\assets\\" + folder + "\\" + filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
    @RequestMapping("/getCourses")
    public ResponseEntity<ApiResponseDTO> getCourses(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                courseService.getCourseDetails()
        ));
    }

}
