package com.mt.api.service;

import com.mt.api.model.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * Service class using internally REST client for remote connection
 */
@Service
public class RestClientServiceImpl implements RestClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientServiceImpl.class);

    private final String languagesUri;
    private final String domainsUri;
    private final String translateUri;
    private final RestClient restClient;

    //Application properties variables to be injected representing remote REST service URLs
    @Autowired
    public RestClientServiceImpl(@Value("${languages.uri}") String languagesUri,
                                 @Value("${domains.uri}") String domainsUri,
                                 @Value("${translate.uri}") String translateUri,
                                 RestClient restClient) {
        this.languagesUri = languagesUri;
        this.domainsUri = domainsUri;
        this.translateUri = translateUri;
        this.restClient = restClient;
    }

    /**
     * Method to retrieve allowed languages collection
     *
     * @return String - representing allowed languages
     */
    public String getAllowedLanguages() {
        LOGGER.info("Trying to fetch allowed languages");
//        return restClient.get().uri(languagesUri).retrieve().body(String.class);
        return restClient.get().uri(languagesUri)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                        LOGGER.warn("No languages retrieved, response status code {}", response.getStatusCode());
                        //We proceed with empty allowed languages collection
                        return "";
                    } else {
                        return Objects.requireNonNull(response.bodyTo(String.class));
                    }
                });
    }

    /**
     * Method to retrieve allowed domains collection
     *
     * @return String - representing allowed domains
     */
    public String getAllowedDomains() {
        LOGGER.info("Trying to fetch allowed domains");
//        return restClient.get().uri(domainsUri).retrieve().body(String.class);
        return restClient.get().uri(domainsUri)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                        LOGGER.warn("No domains retrieved, response status code {}", response.getStatusCode());
                        //We proceed with empty allowed domains collection
                        return "";
                    } else {
                        return Objects.requireNonNull(response.bodyTo(String.class));
                    }
                });
    }

    /**
     * Method to send remote request for translation
     *
     * @param requestMessage - instance of {@link RequestMessage} containing the request body
     * @return ResponseEntity with response details
     */
    public ResponseEntity<Object> postTranslateRequest(RequestMessage requestMessage) {
        return restClient.post().uri(translateUri).contentType(MediaType.APPLICATION_JSON).body(requestMessage)
                .retrieve().toEntity(Object.class);
    }
}
