package com.therame.service;

import com.therame.model.Exercise;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseService {

    Exercise createExercise(Exercise exercise);

    Optional<Exercise> findById(UUID id);

}
