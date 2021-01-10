package com.papers.integration;

import com.papers.domain.Newspapers;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;

import java.util.List;

@Configuration
@RequiredArgsConstructor
class IntegrationConfiguration {

    private final Job job;

    @Bean
    IntegrationFlow integrationFlow(
            NewspapersTransformer transformer,
            JobLaunchingGateway launcher) {
        
        return IntegrationFlows.from(Http.inboundGateway("/convert"))
                .handle(Http.outboundGateway("https://chroniclingamerica.loc.gov/newspapers.json")
                        .charset("UTF-8")
                        .httpMethod(HttpMethod.GET)
                        .mappedRequestHeaders("application/json")
                        .expectedResponseType(Newspapers.class))
                .transform(transformer)
                .transform(message -> request(message, job))
                .handle(launcher)
                .get();
    }

    public JobLaunchRequest request(Object message, Job job) {
        if (message instanceof List) {
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addParameter("list", new NewspaperListJobParameter((List)message));
            return new JobLaunchRequest(job, builder.toJobParameters());
        }
        throw new RuntimeException("bad request");
    }

    @Bean
    public JobLaunchingGateway getGateway(JobLauncher jobLauncher) {
        return new JobLaunchingGateway(jobLauncher);
    }
}
