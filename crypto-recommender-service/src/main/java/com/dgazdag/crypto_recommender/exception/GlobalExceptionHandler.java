package com.dgazdag.crypto_recommender.exception;

import com.dgazdag.crypto_recommender.rest.dto.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.resolve;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildResponseEntity(new ApiErrorDto().status(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleResponseStatusException(NotFoundException ex) {
        return buildResponseEntity(new ApiErrorDto().status(HttpStatus.NOT_FOUND.value()), ex);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(statusCode)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        return buildResponseEntity(new ApiErrorDto().status(statusCode.value()).message(ex.getMessage()), ex);
    }

    private static ResponseEntity<Object> buildResponseEntity(ApiErrorDto apiError, Exception ex) {
        if (resolve(apiError.getStatus()) == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(ex.getMessage(), ex);
        }

        apiError.setMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, resolve(apiError.getStatus()));
    }
}
