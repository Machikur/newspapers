package com.papers.integration;

import com.papers.domain.NewspaperDto;
import org.springframework.batch.core.JobParameter;

import java.util.List;
import java.util.UUID;


public class NewspaperListJobParameter extends JobParameter {

    private List<NewspaperDto> newspaperDtoList;

    public NewspaperListJobParameter(List<NewspaperDto> newspaperDtoList) {
        super(UUID.randomUUID().toString());
        this.newspaperDtoList = newspaperDtoList;
    }

    @Override
    public Object getValue() {
        return newspaperDtoList;
    }
}
