package com.therame.repository.solr;

import com.therame.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@Repository("solrExerciseRepo")
public interface SolrExerciseRepository extends SolrCrudRepository<Exercise, UUID> {

    @Query("description:*?0* OR title:*?0*")
    Page<Exercise> findByCustomQuery(String searchTerm, Pageable pageable);


    List<Exercise> findByTitle(String title);

}
