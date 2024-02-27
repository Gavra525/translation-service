package com.mt.api.service;

import com.mt.api.model.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for request validation
 * Preserves allowed domains and languages
 */
@Service
public class ContentValidationServiceImpl implements ContentValidationService {

    private static final String ALLOWED_DOMAINS = "allowedDomains";
    private static final String ALLOWED_LANGUAGES = "allowedLanguages";

    private final RestClientService restClientService;
    private final Map<String, List<String>> allowedParametersMap;

    public void setAllowedDomains(List<String> allowedDomains) {
        allowedParametersMap.put(ALLOWED_DOMAINS, allowedDomains);
    }

    public List<String> getAllowedDomains() {
        return allowedParametersMap.get(ALLOWED_DOMAINS);
    }

    public void setAllowedLanguages(List<String> allowedLanguages) {
        allowedParametersMap.put(ALLOWED_LANGUAGES, allowedLanguages);
    }

    public List<String> getAllowedLanguages() {
        return allowedParametersMap.get(ALLOWED_LANGUAGES);
    }

    @Autowired
    public ContentValidationServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
        this.allowedParametersMap = new ConcurrentHashMap<>();
    }

    /**
     * Handling request validation for supported language or domain and number of words in a content field
     *
     * @param requestMessage - RequestMessage instance to be validated
     * @return ResponseEntity with status and details
     */
    public ResponseEntity<Object> handleRequest(RequestMessage requestMessage) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        List<String> errors = new ArrayList<>();
        if (!getAllowedLanguages().contains(requestMessage.getSourceLanguage().trim())) {
            errors.add("source_language not supported.");
        }
        if (!getAllowedLanguages().contains(requestMessage.getTargetLanguage().trim())) {
            errors.add("target_language not supported.");
        }
        if (!getAllowedDomains().contains(requestMessage.getDomain().trim())) {
            errors.add("domain not supported.");
        }
        if (!isContentLessThan30Words(requestMessage)) {
            errors.add("content has more than 30 words.");
        }
        //All errors are collected prior returning to the caller for better usability
        if (!errors.isEmpty()) {
            errorResponse.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return restClientService.postTranslateRequest(requestMessage);
    }

    /**
     * Private method to determine if request content contains less than 30 words
     * Internally using StringTokenizer with interpunction and whitespaces characters as potential delimiters
     * Handles word counting properly in the case of interpunction connected with words
     * (example "something,somethingElse" - counted as two words)
     *
     * @param requestMessage - instance of {@link RequestMessage} which content should be validated
     * @return boolean - True in case word count of content is not more than 30, False otherwise
     */
    private boolean isContentLessThan30Words(RequestMessage requestMessage) {
        //Not sure about "-", "_" etc. but I would consider those as part of the word
        StringTokenizer tokenizer = new StringTokenizer(requestMessage.getContent(), " \t\n\r\f:;!?.,()\"");
        return tokenizer.countTokens() <= 30;
    }
}
