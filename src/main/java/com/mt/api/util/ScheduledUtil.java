package com.mt.api.util;

import com.mt.api.service.ContentValidationService;
import com.mt.api.service.ResponseParserService;
import com.mt.api.service.RestClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Component class used for scheduling of remote requests to retrieve languages and domains
 */
@Component
public class ScheduledUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledUtil.class);

    private final RestClientService restClientService;

    private final ResponseParserService responseParserService;

    private final ContentValidationService contentValidationService;

    @Autowired
    public ScheduledUtil(RestClientService restClientService,
                         ResponseParserService responseParserService, ContentValidationService contentValidationService) {
        this.restClientService = restClientService;
        this.responseParserService = responseParserService;
        this.contentValidationService = contentValidationService;
    }

    //Scheduled instead of PostConstruct to allow autowire of this component if needed
    //Required to run on start up as well as application would not be aware of allowed parameters potentially until next day
    @Scheduled(initialDelay = 2000, fixedDelay = Long.MAX_VALUE)
    public void onStartup() {
        retrieveAllowedParameters();
    }

    //Scheduled with application property value
    @Scheduled(cron = "${allowed.parameters.cron.schedule}")
    private void scheduledRun() {
        retrieveAllowedParameters();
    }

    /**
     * Private method to retrieve allowed parameters and preserve them in validation service
     */
    private void retrieveAllowedParameters() {
        LOGGER.info("Retrieving allowed parameters");
        contentValidationService.setAllowedLanguages(responseParserService
                .parseResponse(restClientService.getAllowedLanguages()));
        contentValidationService.setAllowedDomains(responseParserService
                .parseResponse(restClientService.getAllowedDomains()));
    }
}
