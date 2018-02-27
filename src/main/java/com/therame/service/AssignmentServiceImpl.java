package com.therame.service;

import com.therame.model.*;
import com.therame.repository.jpa.AssignmentRepository;
import com.therame.repository.jpa.ExerciseRepository;
import com.therame.repository.jpa.UserRepository;
import com.therame.view.AssignmentView;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "assignments")
public class AssignmentServiceImpl implements AssignmentService {

    private AssignmentRepository assignmentRepository;
    private ExerciseRepository exerciseRepository;
    private UserRepository userRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, ExerciseRepository exerciseRepository,
                                 UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable
    public List<AssignmentView> getForPatientId(UUID patientId) {
        return assignmentRepository.findByPatientId(patientId).stream()
                .map(Assignment::toView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssignmentView createAssignment(UUID patientId, UUID exerciseId, int order) {
        Assignment toCreate = new Assignment();

        User user = userRepository.findOne(patientId);
        Exercise exercise = exerciseRepository.findOne(exerciseId);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        } else if (exercise == null) {
            throw new IllegalArgumentException("Exercise not found!");
        }

        toCreate.setPatient(user);
        toCreate.setExercise(exercise);

        return assignmentRepository.save(toCreate).toView();
    }

    @Override
    public void deleteAssignment(UUID assignmentId) {
        assignmentRepository.delete(assignmentId);
    }

    @CacheEvict(allEntries = true)
    public void clearCache(){}


}
