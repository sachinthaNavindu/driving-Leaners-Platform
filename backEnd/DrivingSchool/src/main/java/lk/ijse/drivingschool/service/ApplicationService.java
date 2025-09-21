package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.ResponseApplicationDTO;
import lk.ijse.drivingschool.dto.StudentApplicationDTO;

import java.io.IOException;
import java.util.List;

public interface ApplicationService {
    String saveApplication(StudentApplicationDTO studentApplicationDTO) throws IOException;
    List<ResponseApplicationDTO> getApplications();
    String approve(Long id,String actionTaken);
}

