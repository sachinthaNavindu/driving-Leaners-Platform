package lk.ijse.drivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponseListDTO<T> {
    private int code;
    private String status;
    private List<T> data;

}
