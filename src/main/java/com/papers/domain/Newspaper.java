package com.papers.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Newspaper {

    @Id
    @GeneratedValue
    private Long id;
    private String lccn;
    private String url;
    private String state;
    private String title;

    public Newspaper(String lccn, String url, String state, String title) {
        this.lccn = lccn;
        this.url = url;
        this.state = state;
        this.title = title;
    }

}
