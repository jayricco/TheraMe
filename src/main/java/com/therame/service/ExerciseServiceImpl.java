package com.therame.service;

import com.therame.model.Exercise;
import com.therame.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

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
        return Optional.of(exerciseRepository.findOne(id));
    }
}
