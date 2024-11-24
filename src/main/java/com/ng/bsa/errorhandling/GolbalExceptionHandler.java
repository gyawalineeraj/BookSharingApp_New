package com.ng.bsa.errorhandling;

import com.ng.bsa.exception.BookAlreadyRegistered;
import com.ng.bsa.exception.InvalidPageNumber;
import com.ng.bsa.response.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@RestControllerAdvice
public class GolbalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handler(
            BadCredentialsException exception) {
        var e = ExceptionResponse.builder()
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handler(
            MessagingException exception) {
        var e = ExceptionResponse.builder()
                .message("Internal Server Error. Please Contact the developer" +
                        " team")
                .build();
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handler(
            SQLIntegrityConstraintViolationException exception) {
        var e = ExceptionResponse.builder()
                .message("You are already registered with that email.Please " +
                        "register with new email or proceed to log in")
                .build();
        return ResponseEntity.badRequest().body(e);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handler(
            NoSuchElementException exception) {
        var e = ExceptionResponse.builder()
                .message("The user with the provided credentials does not " +
                        "exist in the database")
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(e);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handler(
            MethodArgumentNotValidException exception) {
        Set<String> validationErros = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(e -> validationErros.add(e.getDefaultMessage()));
        var e = ExceptionResponse.builder()
                .validationErros(validationErros)
                .error("No Standard credentials, Please try again")
                .build();
        return ResponseEntity.badRequest().body(e);
    }
 @ExceptionHandler(BookAlreadyRegistered.class)
    public ResponseEntity<ExceptionResponse> handler(
            BookAlreadyRegistered exception) {
        var e = ExceptionResponse.builder()
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(e);
    }
@ExceptionHandler(InvalidPageNumber.class)
    public ResponseEntity<ExceptionResponse> handler(
            InvalidPageNumber exception) {
        var e = ExceptionResponse.builder()
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handler(
            ExpiredJwtException exception) {
        var e = ExceptionResponse.builder()
                .error("Your Jwt Token has expired, Please ask for new jwt " +
                        "token ")
                .build();
        return ResponseEntity.badRequest().body(e);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handler(
            Exception exception) {
        var e = ExceptionResponse.builder()
                .message("Internal Server Error. Please Contact the developer" +
                        " team")
                .build();
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(e);
    }

}
