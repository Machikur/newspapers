package com.papers.conventer;

import com.papers.domain.NewspaperDto;
import com.papers.integration.NewspaperDtoListWrapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@StepScope
public class NewspaperListReader implements ItemReader<NewspaperDto> {

    private final List<NewspaperDto> list;
    private final int listSize;
    private int counter = 0;

    public NewspaperListReader(@Value("#{jobParameters}") Map<String, Object> jobParameters) {
        Object result = jobParameters.get("list");
        if (result instanceof NewspaperDtoListWrapper) {
            NewspaperDtoListWrapper newspapers = (NewspaperDtoListWrapper) result;
            this.list = newspapers.getList();
            this.listSize = this.list.size();
        } else {
            this.list = new ArrayList<>();
            this.listSize = 0;
        }
    }

    @Override
    public NewspaperDto read() {
        NewspaperDto newspaperDto = null;
        if (counter < listSize) {
            newspaperDto = list.get(counter);
            counter++;
        } else {
            counter = 0;
        }
        return newspaperDto;
    }

}
