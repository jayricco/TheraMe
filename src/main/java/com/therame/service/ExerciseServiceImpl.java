package com.therame.service;

import com.therame.model.Exercise;
import com.therame.model.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public Optional<Exercise> findById(UUID id) {
        return Optional.ofNullable(exerciseRepository.findOne(id));
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> searchByTitle(String title) {
        return exerciseRepository.findAllByTitle(title);
    }
}
