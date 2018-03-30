package com.therame.service;

import com.therame.model.*;
import com.therame.view.HistoryView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    private AssignmentRepository assignmentRepository;
    private ExerciseRepository exerciseRepository;
    private UserRepository userRepository;

    public HistoryServiceImpl(AssignmentRepository assignmentRepository, ExerciseRepository exerciseRepository,
                              UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<HistoryView> getHistoryForPatientId(UUID patientId){
        return historyRepository.findByPatientId(patientId).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    List<HistoryView> getForAllPatients();

    HistoryView addFeedback(UUID patientId, UUID exerciseId, String feedback);

    HistoryView createHistory(UUID patientId, UUID exerciseId, Date date);



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
}
