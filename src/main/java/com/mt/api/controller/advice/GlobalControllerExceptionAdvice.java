package com.mt.api.controller.advice;

import com.mt.api.controller.ContentValidationController;
import com.mt.api.controller.MachineTranslationServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ContentValidationController exception advice class
 * Formats the response with errors field in case of MethodArgumentNotValidException
 * (additional annotations of the RequestMessage class make sure the exception is thrown when needed)
 * <p>
 * The response errors field will contain all the validation errors with single request for better usability
 */
@RestControllerAdvice(assignableTypes = ContentValidationController.class)
public class GlobalControllerExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorsMap(ex.getBindingResult().getFieldErrors()));
    }

    private Map<String, List<String>> getErrorsMap(List<FieldError> fieldErrors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", fieldErrors.stream().map(FieldError::getDefaultMessage).toList());
        return errorResponse;
    }
}
