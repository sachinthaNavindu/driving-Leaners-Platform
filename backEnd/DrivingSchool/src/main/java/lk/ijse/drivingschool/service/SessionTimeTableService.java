package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.SessionInfoDTO;
import lk.ijse.drivingschool.entity.InstructorRespond;
import lk.ijse.drivingschool.repository.PendingSessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionTimeTableService {

    private final PendingSessionRepo pendingSessionRepo;

    public Object generateSessionId() {
        String lastId = pendingSessionRepo.getLastSessionId();
        String nextId;
        if (lastId == null) {
            nextId = "S001";
        }else {
            int numericId = Integer.parseInt(lastId.substring(1));
            nextId = String.format("S%03d", numericId+1);
        }

        long pendingCount = pendingSessionRepo.countAllByRespond(InstructorRespond.PENDING);

        long ApprovedCount = pendingSessionRepo.countAllByRespond(InstructorRespond.ACCEPTED);

        return new SessionInfoDTO(nextId, pendingCount, ApprovedCount);

    }
}
