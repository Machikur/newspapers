package com.papers.conventer;

import com.papers.domain.Newspaper;
import com.papers.domain.NewspaperDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final NewspaperListReader reader;

    @Bean
    ItemProcessor<NewspaperDto, Newspaper> processor() {
        return dto -> new Newspaper(dto.getLccn(), dto.getUrl(), dto.getState(), dto.getTitle());
    }

    @Bean
    HibernateItemWriter<Newspaper> writer() {
        return new HibernateItemWriter<>();
    }

    @Bean
    Step changeToEntity(
            ItemProcessor<NewspaperDto, Newspaper> processor,
            HibernateItemWriter<Newspaper> writer
    ) {
        return stepBuilderFactory.get("convertToEntity")
                .<NewspaperDto, Newspaper>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    Job saveNewspapers(Step changeToEntity) {
        return jobBuilderFactory.get("saveNewspapers")
                .incrementer(new RunIdIncrementer())
                .flow(changeToEntity)
                .end()
                .build();
    }

}
