package com.mt.api.service;

import com.mt.api.model.RequestMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContentValidationService {

    public void setAllowedDomains(List<String> allowedDomains);

    public List<String> getAllowedDomains();

    public void setAllowedLanguages(List<String> allowedLanguages);

    public List<String> getAllowedLanguages();

    public ResponseEntity<Object> handleRequest(RequestMessage requestMessage);
}
