package com.essayservice.jobs;

import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.services.essayparsingrequest.EssayParsingRequestService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Log4j2
@Service
@PropertySource("classpath:/config.properties")
@NoArgsConstructor
@Data
public class EssayParseJob {
    private int parseRequestLimit;
    private int numberOfThreadsInPool;
    private EssayParsingRequestService essayParsingRequestService;
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    public EssayParseJob(@Value("${essay.parse.job.parallel.request.limit}") int parseRequestLimit,
                         @Value("${essay.parse.job.threads.number}") int numberOfThreadsInPool,
                         EssayParsingRequestService essayParsingRequestService) {
        this.parseRequestLimit = parseRequestLimit;
        this.numberOfThreadsInPool = numberOfThreadsInPool;
        this.essayParsingRequestService = essayParsingRequestService;
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreadsInPool);

    }

    @Scheduled(cron = "${essay.parse.job.schedule.crone.expression}")
    public void parseReadyEssays() {
        log.info("Parse essay job had started");
        List<EssayParsingRequest> parseRequests = essayParsingRequestService.getReadyParseRequests(parseRequestLimit);
        int parseRequestsSize = parseRequests != null ? parseRequests.size() : 0;
        log.info("Parse essay job had got " + parseRequestsSize + " parse requests");
        if (parseRequests != null && !parseRequests.isEmpty()) {
            for (EssayParsingRequest essayParseRequest : parseRequests) {
                threadPoolExecutor.execute(() -> essayParsingRequestService.handleParseRequest(essayParseRequest));
            }
        }
        log.info("Parse essay job had finished");
    }
}
