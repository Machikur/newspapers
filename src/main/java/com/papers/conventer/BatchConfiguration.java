package com.papers.conventer;

import com.papers.domain.Newspaper;
import com.papers.domain.NewspaperDto;
import com.papers.repository.NewspaperRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final NewspaperRepo repo;

    @Bean
    ItemProcessor<NewspaperDto, Newspaper> processor() {
        return dto -> new Newspaper(dto.getLccn(), dto.getUrl(), dto.getState(), dto.getTitle());
    }

    @Bean
    ItemWriter<Newspaper> writer() {
        return repo::saveAll;
    }

    @Bean
    Step changeToEntity(
            NewspaperListReader reader,
            ItemProcessor<NewspaperDto, Newspaper> processor,
            ItemWriter<Newspaper> writer
    ) {
        return stepBuilderFactory.get("changeToEntity")
                .<NewspaperDto, Newspaper>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @Qualifier("SaveNewspapers")
    Job saveNewspapers(Step changeToEntity) {
        return jobBuilderFactory.get("saveNewspapers")
                .incrementer(new RunIdIncrementer())
                .flow(changeToEntity)
                .end()
                .build();
    }

}
