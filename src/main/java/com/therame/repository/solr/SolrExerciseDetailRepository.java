package com.therame.repository.solr;

import com.therame.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("solrExerciseRepo")
public interface SolrExerciseDetailRepository extends SolrCrudRepository<Exercise, UUID> {
    Page<Exercise> findByTitle(String title, Pageable pageable);


    Page<Exercise> findExercisesByTitleOrDescription(String title, String description, Pageable pageable);
}
