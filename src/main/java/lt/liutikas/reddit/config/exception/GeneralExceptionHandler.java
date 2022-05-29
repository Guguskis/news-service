package lt.liutikas.reddit.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(processFieldErrorsMessage(ex));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationException(AuthorizationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private String processFieldErrorsMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> "'" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

}