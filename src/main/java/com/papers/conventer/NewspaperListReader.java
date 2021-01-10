package com.papers.conventer;

import com.google.gson.Gson;
import com.papers.domain.NewspaperDto;
import com.papers.domain.Newspapers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@StepScope
@Component
public class NewspaperListReader implements ItemReader<NewspaperDto> {

    private final List<NewspaperDto> list;
    private final int listSize;
    private int counter = 0;

    public NewspaperListReader(@Value("#{jobParameters}") Map<String, Object> jobParameters) throws IOException, ClassNotFoundException {
        Object result = jobParameters.get("list");
        if (result instanceof String) {
           Gson gson=new Gson();
            Newspapers newspapers = gson.fromJson(result.toString(),Newspapers.class);
            this.list = newspapers.getNewspapers();
            listSize = this.list.size();
        } else {
            list = new ArrayList<>();
            listSize = 0;
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
