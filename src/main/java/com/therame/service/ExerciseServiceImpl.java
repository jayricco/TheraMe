package com.therame.service;

import com.therame.model.Exercise;
import com.therame.repository.jpa.ExerciseRepository;
import com.therame.repository.solr.SolrExerciseDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepo;

    @Autowired
    private SolrExerciseDetailRepository solrExerciseRepo;


    @Override
    public Exercise createExercise(Exercise exercise) {
        solrExerciseRepo.save(exercise);
        return exerciseRepo.save(exercise);

    }

    @Override
    public Optional<Exercise> findById(UUID id) {
        return Optional.ofNullable(exerciseRepo.findOne(id));
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseRepo.findAll();
    }

    @Override
    public List<Exercise> searchByTitle(String title) {
        return solrExerciseRepo.findByTitle(title, new PageRequest(0, 10)).getContent();
    }
}
