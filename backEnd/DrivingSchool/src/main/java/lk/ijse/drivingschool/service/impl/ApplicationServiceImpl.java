package lk.ijse.drivingschool.service.impl;

import lk.ijse.drivingschool.dto.ResponseApplicationDTO;
import lk.ijse.drivingschool.dto.StudentApplicationDTO;
import lk.ijse.drivingschool.entity.Application;
import lk.ijse.drivingschool.entity.enums.ApplicationRespond;
import lk.ijse.drivingschool.repository.ApplicationRepo;
import lk.ijse.drivingschool.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepo applicationRepo;

    @Override
    public String saveApplication(StudentApplicationDTO studentApplicationDTO) throws IOException {
        String frontFileName = UUID.randomUUID() + "_" + studentApplicationDTO.getNicFront().getOriginalFilename();
        String backFileName = UUID.randomUUID() + "_" + studentApplicationDTO.getNicBack().getOriginalFilename();
        String paymentSlip = UUID.randomUUID() + "_" + studentApplicationDTO.getPaymentSlip().getOriginalFilename();

        String uploadDir = new File("D:\\Ijse\\Aad\\finalProject\\frontEnd\\assets\\imagesDB").getAbsolutePath();

        Path frontPath = Paths.get(uploadDir, frontFileName);
        Path backPath = Paths.get(uploadDir, backFileName);
        Path SlipPath = Paths.get(uploadDir, paymentSlip);

        Files.write(frontPath, studentApplicationDTO.getNicFront().getBytes());
        Files.write(backPath, studentApplicationDTO.getNicBack().getBytes());
        Files.write(SlipPath, studentApplicationDTO.getPaymentSlip().getBytes());

        String frontUrl = "/imagesDB/" + frontFileName;
        String backUrl = "/imagesDB/" + backFileName;
        String slipUrl = "/imagesDB/" + paymentSlip;

        Application application = Application.builder()
                .nic(studentApplicationDTO.getNic())
                .fullName(studentApplicationDTO.getName())
                .email(studentApplicationDTO.getEmail())
                .contact(studentApplicationDTO.getContact())
                .address(studentApplicationDTO.getAddress())
                .paidAmount(Double.valueOf(studentApplicationDTO.getPaidAmount()))
                .courseName(studentApplicationDTO.getCourseName())
                .nicFrontUrl(frontUrl)
                .nicBackUrl(backUrl)
                .paymentSlip(slipUrl)
                .respond(ApplicationRespond.PENDING)
                .build();

        applicationRepo.save(application);

        return "Application saved successfully!";
    }

    @Override
    public List<ResponseApplicationDTO> getApplications() {
        return applicationRepo.findAllByRespond(ApplicationRespond.PENDING)
                .stream()
                .map(app -> ResponseApplicationDTO.builder()
                        .id(app.getId())
                        .name(app.getFullName())   // map fullName -> name
                        .nic(app.getNic())
                        .email(app.getEmail())
                        .contact(app.getContact())
                        .address(app.getAddress())
                        .courseName(app.getCourseName())
                        .paidAmount(app.getPaidAmount() != null ? app.getPaidAmount().toString() : null)
                        .nicFrontUrl(app.getNicFrontUrl())
                        .nicBackUrl(app.getNicBackUrl())
                        .paymentSlip(app.getPaymentSlip())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public String approve(Long id,String actionTaken) {
        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        application.setRespond(ApplicationRespond.valueOf(actionTaken));

        applicationRepo.save(application);
        return "Application "+actionTaken+" successfully!";
    }

}
