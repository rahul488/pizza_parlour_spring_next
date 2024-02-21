package com.pizzaparlour.backend.Exception;

import com.pizzaparlour.backend.Dto.Response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex){
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setMessage(ex.getMessage());
            errorResponseDto.setStatus(false);
            errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

            return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> productNotFoundException(ProductNotFoundException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setStatus(false);
        errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> invalidLogin(InvalidCredentialsException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setStatus(false);
        errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()));

        return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> isEmailExist(EmailAlreadyExistException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setStatus(false);
        errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()));
        return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> customerNotFound(CustomerNotFoundException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setStatus(false);
        errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()));
        return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorResponseDto> paymentFailed(PaymentFailedException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setStatus(false);
        errorResponseDto.setHttpStatusCode(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(errorResponseDto,HttpStatus.OK);
    }
}