package com.mt.api.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Service class for parsing domains and languages from response
 * Internally uses StringTokenizer with provided list of delimiters that is expected
 */
@Service
public class ResponseParserServiceImpl implements ResponseParserService {

    public List<String> parseResponse(String response) {
        List<String> responseList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(response, " \t\n\r\f,\"{}");
        while (tokenizer.hasMoreTokens()) {
            responseList.add(tokenizer.nextToken());
        }
        return responseList;
    }
}
