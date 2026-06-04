package com.queue.smart_queue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CounterInServiceException.class)
    public ResponseEntity<ErrorResponseFormat> handleException(CounterInServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseFormat(
                        LocalDateTime.now(),
                        ex.getMessage()));
    }

    @ExceptionHandler(TicketNotCalledException.class)
    public ResponseEntity<ErrorResponseFormat> handleException(TicketNotCalledException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseFormat(
                        LocalDateTime.now(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(CounterNotFoundOrOfflineException.class)
    public ResponseEntity<ErrorResponseFormat> handleException(CounterNotFoundOrOfflineException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseFormat(
                        LocalDateTime.now(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ErrorResponseFormat> handleException(NotAllowedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseFormat(
                        LocalDateTime.now(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponseFormat> handleException(TicketNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseFormat(
                        LocalDateTime.now(),
                        ex.getMessage()
                ));
    }
}
