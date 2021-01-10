package com.papers.integration;

import com.papers.domain.Newspaper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewspaperRepo extends CrudRepository<Newspaper, Long> {
}
