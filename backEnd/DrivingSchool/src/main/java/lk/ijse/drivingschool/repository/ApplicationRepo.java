package lk.ijse.drivingschool.repository;

import io.micrometer.common.KeyValues;
import lk.ijse.drivingschool.dto.ResponseApplicationDTO;
import lk.ijse.drivingschool.entity.Application;
import lk.ijse.drivingschool.entity.enums.ApplicationRespond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

    List<Application> findAllByRespond(ApplicationRespond respond);



}
