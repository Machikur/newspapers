package com.papers.repository;

import com.papers.domain.Newspaper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewspaperRepo extends CrudRepository<Newspaper, Long> {

    List<Newspaper> findAll();


}
