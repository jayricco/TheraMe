package com.therame.service;

import com.therame.model.DetailedUserDetails;
import com.therame.model.Exercise;
import com.therame.repository.jpa.ExerciseRepository;
import com.therame.model.Provider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Optional<Exercise> findExerciseById(UUID id) {
        return Optional.ofNullable(exerciseRepository.findOne(id));
    }

    @Override
    public List<Exercise> findAllExercises() {
        DetailedUserDetails currentUser = (DetailedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Provider currentUserProvider = currentUser.getUser().getProvider();

        return exerciseRepository.findAll().stream()
                .filter(exercise -> currentUserProvider == null || exercise.getProvider() == null || currentUserProvider.equals(exercise.getProvider()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Exercise> searchExercisesByTitle(String title) {
        DetailedUserDetails currentUser = (DetailedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Provider currentUserProvider = currentUser.getUser().getProvider();

        return exerciseRepository.findAllByTitle(title).stream()
                .filter(exercise -> currentUserProvider == null || exercise.getProvider() == null || currentUserProvider.equals(exercise.getProvider()))
                .collect(Collectors.toList());
    }
}
