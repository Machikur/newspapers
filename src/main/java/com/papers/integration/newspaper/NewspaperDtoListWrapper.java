package com.papers.integration.newspaper;

import com.papers.domain.NewspaperDto;

import java.io.Serializable;
import java.util.List;

public class NewspaperDtoListWrapper implements Serializable {

    private final List<NewspaperDto> list;

    public NewspaperDtoListWrapper(List<NewspaperDto> list) {
        this.list = list;
    }

    public List<NewspaperDto> getList() {
        return list;
    }

}
