package com.papers.integration.newspaper;

import com.papers.domain.NewspaperDto;
import com.papers.domain.Newspapers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;

import java.util.List;

import static com.papers.integration.newspaper.Headers.RESPONSE_HEADER;

@Configuration
@RequiredArgsConstructor
@Slf4j
class IntegrationConfiguration {

    @Qualifier("saveNewspapers")
    private final Job job;

    @Bean
    IntegrationFlow integrationFlow(JobLaunchingGateway launcher, NotificationRouter router) {
        return IntegrationFlows.from(Http.inboundGateway("/convert")
                .headerExpression(RESPONSE_HEADER, "#requestParams['notifyType'] != null? #requestParams['notifyType'][0] : null"))
                .handle(Http.outboundGateway("https://chroniclingamerica.loc.gov/newspapers.json")
                        .charset("UTF-8")
                        .httpMethod(HttpMethod.GET)
                        .mappedRequestHeaders("application/json")
                        .expectedResponseType(Newspapers.class))
                .transform(Newspapers::getNewspapers)
                .transform(message -> request(message, job))
                .handle(launcher)
                .route(router)
                .get();

    }

    @Bean
    public JobLaunchingGateway getGateway(JobLauncher jobLauncher) {
        return new JobLaunchingGateway(jobLauncher);
    }

    private JobLaunchRequest request(Object message, Job job) {
        if (message instanceof List<?>) {
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addParameter("list", new NewspaperJobParameter((List<NewspaperDto>) message));
            return new JobLaunchRequest(job, builder.toJobParameters());
        }
        throw new RuntimeException("bad request");
    }

}
