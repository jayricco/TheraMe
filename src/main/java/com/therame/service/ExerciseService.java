package com.therame.service;

import com.therame.model.Exercise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseService {

    Exercise createExercise(Exercise exercise);

    Optional<Exercise> findById(UUID id);

    List<Exercise> findAll();

    List<Exercise> searchByTitle(String title);

    Optional<Exercise> findByTitle(String title);

    Optional<Exercise> updateExercise(Exercise exercise);
}
