package com.papers.integration.newspaper;

import com.papers.domain.NewspaperDto;
import org.springframework.batch.core.JobParameter;

import java.util.List;
import java.util.UUID;

public class NewspaperJobParameter extends JobParameter {

    private final List<NewspaperDto> list;

    public NewspaperJobParameter(List<NewspaperDto> list) {
        super(UUID.randomUUID().toString());
        this.list = list;
    }

    @Override
    public Object getValue() {
        return new NewspaperDtoListWrapper(list);
    }

}
