package com.papers.integration;

import com.papers.domain.NewspaperDto;
import com.papers.domain.Newspapers;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewspapersTransformer implements GenericTransformer<Newspapers, List<NewspaperDto>> {

    @Override
    public List<NewspaperDto> transform(Newspapers source) {
        return source.getNewspapers();
    }


}
