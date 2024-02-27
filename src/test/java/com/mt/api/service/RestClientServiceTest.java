package com.mt.api.service;

import com.mt.api.constants.ApiConstants;
import com.mt.api.model.RequestMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RestClientServiceTest {

    @Autowired
    RestClientService restClientService;

    @MockBean
    private RestClient restClient;

    @Test
    void testAllowedLanguages() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpecLang = Mockito.mock(RestClient.RequestHeadersSpec.class);

        String expectedResponseLang = ApiConstants.LANGUAGE_VALUES;
        String languagesUrl = ApiConstants.LANGUAGES;
        String expectedResponse = ApiConstants.LANGUAGE_VALUES;

        //Mocking RestClient usage
        Mockito.when(restClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(ArgumentMatchers.contains(languagesUrl))).thenReturn(requestHeadersSpecLang);
        Mockito.when(requestHeadersSpecLang.exchange(ArgumentMatchers.any())).thenReturn(expectedResponseLang);

        String result = restClientService.getAllowedLanguages();
        assertEquals(expectedResponse, result);
    }

    @Test
    void testAllowedDomains() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpecDomain = Mockito.mock(RestClient.RequestHeadersSpec.class);

        String expectedResponseDomain = ApiConstants.DOMAIN_VALUES;
        String domainUrl = ApiConstants.DOMAINS;
        String expectedResponse = ApiConstants.DOMAIN_VALUES;

        //Mocking RestClient usage
        Mockito.when(restClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(ArgumentMatchers.contains(domainUrl))).thenReturn(requestHeadersSpecDomain);
        Mockito.when(requestHeadersSpecDomain.exchange(ArgumentMatchers.any())).thenReturn(expectedResponseDomain);

        String result = restClientService.getAllowedDomains();
        assertEquals(expectedResponse, result);
    }

    @Test
    void testPostTranslateRequest() {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = Mockito.mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = Mockito.mock(RestClient.ResponseSpec.class);
        RequestMessage requestMessage = new RequestMessage(
                "it-IT", "en-US", "creative", "Translatable content.");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.ACCEPTED).body(requestMessage);

        //Mocking RestClient usage
        Mockito.when(restClient.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri(ArgumentMatchers.contains(ApiConstants.TRANSLATE))).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.contentType(ArgumentMatchers.any(MediaType.class))).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.body(requestMessage)).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.toEntity(Object.class)).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(requestMessage));

        ResponseEntity<Object> result = restClientService.postTranslateRequest(requestMessage);
        assertEquals(expectedResponse, result);
    }
}