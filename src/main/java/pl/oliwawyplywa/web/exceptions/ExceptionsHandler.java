package pl.oliwawyplywa.web.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(HTTPException.class)
    public ResponseEntity<Object> handleException(HTTPException ex) {
        return ResponseEntity.ok().body(Map.of(
            "status", false, "message", ex.getMessage()
        ));
    }

}
