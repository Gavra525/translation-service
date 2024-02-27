package com.mt.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mt.api.constants.ApiConstants;
import com.mt.api.model.RequestMessage;
import com.mt.api.service.ContentValidationService;
import com.mt.api.service.RestClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class to validate /validated-translate API responses
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ValidatedTranslateTest {

    private static final String LOREM_IPSUM_WITH_INTERPUNCTION = "Lorem ipsum dolor sit amet,consectetur adipiscing " +
            "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis " +
            "nostrud exercitation ullamco laboris nisi:ut aliquip ex ea commodo consequat!Duis aute irure dolor in " +
            "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur?Excepteur sint occaecat " +
            "cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    public static final String THIRTY_WORDS = "One two three four five six seven eight nine ten eleven twelve " +
            "thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty twentyOne twentyTwo twentyThree " +
            "twentyFour twentyFive twentySix twentySeven twentyEight twentyNine Thirty";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestClientService restClientService;

    @Autowired
    ContentValidationService contentValidationService;

    @BeforeEach
    public void initialize() {
        List<String> languageList = new ArrayList<>();
        List<String> domainList = new ArrayList<>();
        languageList.add("en-US");
        languageList.add("en-GB");
        languageList.add("fr-FR");
        languageList.add("de-DE");
        languageList.add("it-IT");
        languageList.add("es-ES");
        domainList.add("academic");
        domainList.add("business");
        domainList.add("general");
        domainList.add("casual");
        domainList.add("creative");

        contentValidationService.setAllowedDomains(domainList);
        contentValidationService.setAllowedLanguages(languageList);
    }

    @Test
    void testRequiredParametersMissing() throws Exception {
        ResultActions actions = performValidatedTranslateRequest(
                new RequestMessage(null, "", "", null));
        actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("source_language")))
                .andExpect(content().string(containsString("target_language")))
                .andExpect(content().string(containsString("domain")))
                .andExpect(content().string(containsString("content")));
    }

    @Test
    void testInvalidSourceLanguage() throws Exception {
        ResultActions actions = performValidatedTranslateRequest(new RequestMessage(
                "rs-RS", "en-US", "casual", "Translatable content."));
        actions
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("source_language")));
    }

    @Test
    void testInvalidTargetLanguage() throws Exception {
        ResultActions actions = performValidatedTranslateRequest(new RequestMessage(
                "en-US", "rs-RS", "casual", "Translatable content."));
        actions
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("target_language")));
    }

    @Test
    void testInvalidDomain() throws Exception {
        ResultActions actions = performValidatedTranslateRequest(new RequestMessage(
                "en-US", "de-DE", "domain", "Translatable content."));
        actions
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("domain")));
    }

    @Test
    void testContentMoreThan30Words() throws Exception {
        ResultActions actions = performValidatedTranslateRequest(new RequestMessage(
                "en-US", "de-DE", "creative", LOREM_IPSUM_WITH_INTERPUNCTION));
        actions
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("content")));
    }

    @Test
    void testContentExactly30Words() throws Exception {
        RequestMessage requestMessage = new RequestMessage(
                "it-IT", "en-US", "creative", THIRTY_WORDS);

        //Mocking external restClientService call
        Mockito.when(restClientService.postTranslateRequest(requestMessage))
                .thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(requestMessage));
        ResultActions actions = performValidatedTranslateRequest(requestMessage);
        actions
                .andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testAllValidParameters() throws Exception {
        RequestMessage requestMessage = new RequestMessage(
                "it-IT", "en-US", "creative", "Translatable content.");

        //Mocking external restClientService call
        Mockito.when(restClientService.postTranslateRequest(requestMessage))
                .thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(requestMessage));
        ResultActions actions = performValidatedTranslateRequest(requestMessage);
        actions
                .andDo(print()).andExpect(status().is2xxSuccessful());
    }

    private ResultActions performValidatedTranslateRequest(RequestMessage requestMessage) throws Exception {
        return mockMvc.perform(post(ApiConstants.API + ApiConstants.VERSION_1 + ApiConstants.VALIDATED_TRANSLATE)
                .content(asJsonString(requestMessage))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
