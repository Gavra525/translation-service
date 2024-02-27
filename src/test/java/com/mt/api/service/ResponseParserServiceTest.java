package com.mt.api.service;

import com.mt.api.constants.ApiConstants;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class ResponseParserServiceTest {

    @Test
    public void testParseResponse() {
        //GIVEN
        String response = "{\"first\",\"second\",   \"third\",\n\t\"forth\", \"fifth\"}";
        ResponseParserService parserService = new ResponseParserServiceImpl();
        List<String> expected = Arrays.asList("first", "second", "third", "forth", "fifth");

        //WHEN
        List<String> actual = parserService.parseResponse(response);

        //THEN
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguageParseResponse() {
        //GIVEN
        String response = ApiConstants.LANGUAGE_VALUES;
        ResponseParserService parserService = new ResponseParserServiceImpl();
        List<String> expected = Arrays.asList("en-US", "en-GB", "fr-FR", "de-DE", "it-IT", "es-ES");

        //WHEN
        List<String> actual = parserService.parseResponse(response);

        //THEN
        assertEquals(expected, actual);
    }

    @Test
    public void testDomainParseResponse() {
        //GIVEN
        String response = ApiConstants.DOMAIN_VALUES;
        ResponseParserService parserService = new ResponseParserServiceImpl();
        List<String> expected = Arrays.asList("academic", "business", "general", "casual", "creative");

        //WHEN
        List<String> actual = parserService.parseResponse(response);

        //THEN
        assertEquals(expected, actual);
    }
}