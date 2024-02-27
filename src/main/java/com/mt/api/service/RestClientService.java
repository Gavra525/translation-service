package com.mt.api.service;

import com.mt.api.model.RequestMessage;
import org.springframework.http.ResponseEntity;

public interface RestClientService {

    public String getAllowedLanguages();

    public String getAllowedDomains();

    public ResponseEntity<Object> postTranslateRequest(RequestMessage requestMessage);
}
