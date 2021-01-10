package com.papers.integration;

import com.papers.domain.NewspaperDto;
import org.springframework.batch.core.JobParameter;

import java.util.List;
import java.util.UUID;

public class NewspaperJobParametr extends JobParameter {

    private String value;

    public NewspaperJobParametr(String value) {
        super(UUID.randomUUID().toString());
        this.value=value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isIdentifying() {
        return false;
    }
}
