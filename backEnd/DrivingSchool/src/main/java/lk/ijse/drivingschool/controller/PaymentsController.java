package lk.ijse.drivingschool.controller;

import lk.ijse.drivingschool.dto.ApiResponseDTO;
import lk.ijse.drivingschool.service.PaymentService;
import lk.ijse.drivingschool.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final StudentService studentService;
    private final PaymentService paymentService;

    @RequestMapping("/loadPayments")
    public ResponseEntity<ApiResponseDTO>getAllPayments(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                paymentService.getAllPayment()
        ));
    }

    @RequestMapping("/deletePayment/{paymentId}")
    public ResponseEntity<ApiResponseDTO> deletePayment(@PathVariable("paymentId") String paymentId){

        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                paymentService.deletePayment(paymentId)
        ));
    }

    @RequestMapping("/loadInfluencePayments")
    public ResponseEntity<ApiResponseDTO>getInfluencePayments(){
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                paymentService.getPaymentsWithYetToPay()
        ));
    }
}
