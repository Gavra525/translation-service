package com.mt.api.controller;

import com.mt.api.constants.ApiConstants;
import com.mt.api.model.RequestMessage;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Given REST API endpoints of Machine Translation Service
 */
@RestController
@RequestMapping(value = ApiConstants.API + ApiConstants.VERSION_1,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MachineTranslationServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineTranslationServiceController.class);

    @GetMapping(value = ApiConstants.LANGUAGES)
    public ResponseEntity<Object> getLanguages() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiConstants.LANGUAGE_VALUES);
    }

    @GetMapping(value = ApiConstants.DOMAINS)
    public ResponseEntity<Object> getDomains() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiConstants.DOMAIN_VALUES);
    }

    @PostMapping(value = ApiConstants.TRANSLATE)
    public ResponseEntity<Object> translate(@Valid @RequestBody RequestMessage requestMessage) {
        LOGGER.info("Request message {}", requestMessage.toString());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestMessage);
    }
}
