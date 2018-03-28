package com.therame.service;

import com.therame.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

public interface ExerciseService {

    Exercise createExercise(Exercise exercise);

    Optional<Exercise> findExerciseById(UUID id);

    List<Exercise> findAllExercises();

    Future<List<Exercise>> searchExercisesByTitle(String title);

}
