package com.mt.api.controller;

import com.mt.api.constants.ApiConstants;
import com.mt.api.model.RequestMessage;
import com.mt.api.service.ContentValidationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * /content-validation controller class
 * Exposing required POST REST API for validated translate
 */
@RestController
@RequestMapping(value = ApiConstants.API + ApiConstants.VERSION_1,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ContentValidationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentValidationController.class);

    private final ContentValidationService contentValidationService;

    @Autowired
    public ContentValidationController(ContentValidationService contentValidationService) {
        this.contentValidationService = contentValidationService;
    }

    @PostMapping(value = ApiConstants.VALIDATED_TRANSLATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> translate(@Valid @RequestBody RequestMessage requestMessage) {
        LOGGER.info("Request message {}", requestMessage.toString());
        return contentValidationService.handleRequest(requestMessage);
    }
}
