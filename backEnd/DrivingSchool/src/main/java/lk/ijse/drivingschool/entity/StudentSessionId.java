package lk.ijse.drivingschool.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSessionId implements Serializable {
    private String studentNic;
    private String sessionId;
}
