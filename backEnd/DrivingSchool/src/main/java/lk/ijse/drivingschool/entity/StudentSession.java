package lk.ijse.drivingschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSession {

    @EmbeddedId
    private StudentSessionId id;

    @ManyToOne
    @MapsId("studentNic")
    @JoinColumn(name = "nic")
    private Student student;

    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private SessionTimeTable session;
    @Column(columnDefinition = "BOOLEAN")
    private Boolean attendance;
}
