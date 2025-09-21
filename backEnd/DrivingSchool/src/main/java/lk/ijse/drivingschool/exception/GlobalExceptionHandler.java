package lk.ijse.drivingschool.exception;

import lk.ijse.drivingschool.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseDTO handleNicNotFoundException(UsernameNotFoundException e) {
        return new ApiResponseDTO(
                404,
                "User Not Found",
                e.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponseDTO handleBadCredentialsException(BadCredentialsException e) {
        return new ApiResponseDTO(
                401,
                "Unauthorized",
                "IInvalid Username or password"
        );
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDTO handleRuntimeException(RuntimeException e){
        return new ApiResponseDTO(
                400,
                "Bad Request",
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ApiResponseDTO(400, errors, null);
    }
}
