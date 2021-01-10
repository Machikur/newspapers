package com.papers.conventer;

import com.papers.domain.NewspaperDto;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewspaperListReader implements ItemReader<NewspaperDto> {

    private final List<NewspaperDto> list;
    private final int listSize;
    private int counter = 0;

    public NewspaperListReader(@Value("#{jobParameters}")JobParameters jobParameters) {
        Object list = jobParameters.getParameters().get("list");
        this.list = (List<NewspaperDto>) list;
        listSize = this.list.size();
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
