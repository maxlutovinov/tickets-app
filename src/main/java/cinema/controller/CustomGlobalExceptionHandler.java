package cinema.controller;

import cinema.exception.DataProcessingException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());
        Map<String, Object> body = getResponseBody(status, errorMessage, request);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({DataProcessingException.class, NoSuchElementException.class})
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex,
                                                            WebRequest request) {
        HttpStatus status = (ex.getClass() == DataProcessingException.class)
                ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.NOT_FOUND;
        Map<String, Object> body = getResponseBody(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }

    private Map<String, Object> getResponseBody(HttpStatus status,
                                                Object errorMessage,
                                                WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", errorMessage);
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return body;
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            return field + " " + e.getDefaultMessage();
        }
        return e.getDefaultMessage();
    }
}
