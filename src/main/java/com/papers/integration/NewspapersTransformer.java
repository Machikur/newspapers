package com.papers.integration;

import com.papers.domain.NewspaperDto;
import com.papers.domain.Newspapers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NewspapersTransformer implements GenericTransformer<Newspapers, List<NewspaperDto>> {

    @SneakyThrows
    @Override
    public List<NewspaperDto> transform(Newspapers source) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(source);
        objectOutputStream.flush();
        objectOutputStream.close();
        return source.getNewspapers();
    }


}
