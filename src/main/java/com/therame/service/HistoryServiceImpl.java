package com.therame.service;

import com.therame.model.*;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    private AssignmentRepository assignmentRepository;
    private ExerciseRepository exerciseRepository;
    private UserRepository userRepository;
    private HistoryRepository historyRepository;
    private FeedbackRepository feedbackRepository;

    public HistoryServiceImpl(AssignmentRepository assignmentRepository, ExerciseRepository exerciseRepository,
                              UserRepository userRepository, HistoryRepository historyRepository,
                              FeedbackRepository feedbackRepository) {
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<HistoryView> getHistoryForPatientId(UUID patientId){
        return historyRepository.findByPatientId(patientId).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryView> getForAllPatients(UUID therapistId){
        return historyRepository.findByTherapistId(therapistId).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackView> getFeedbackForPatientId(UUID patientId){
        return feedbackRepository.findByPatientId(patientId).stream()
                .map(Feedback::toView)
                .collect(Collectors.toList());
    }

    //need to fix this
    @Override
    @Transactional
    public FeedbackView addFeedback(UUID patientId, UUID exerciseId, String feedback){
        Feedback toAdd = new Feedback();
        User user = userRepository.findOne(patientId);
        Exercise exercise = exerciseRepository.findOne(exerciseId);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        else if (exercise == null) {
            throw new IllegalArgumentException("Exercise not found!");
        }
        else if (feedback == null || feedback.isEmpty()) {
            throw new IllegalArgumentException("Feedback not provided!");
        }

        toAdd.setComments(feedback);

        return feedbackRepository.save(toAdd).toView();
    }

    @Override
    @Transactional
    public HistoryView addHistory(UUID patientId, UUID assignmentId) {
        System.out.println("adding");
        Assignment assignment = Objects.requireNonNull(assignmentRepository.findOne(assignmentId));
        User patient = Objects.requireNonNull(userRepository.findOne(patientId));

        History toAdd = new History();

        // I'm not sure what start time is useful for, so just setting it at the same time as end time for now...
        toAdd.setTimeStart(new Date());
        toAdd.setTimeEnd(new Date());
        toAdd.setAssignment(assignment);
        toAdd.setPatient(patient);
        toAdd.setTherapist(patient.getTherapist());
        toAdd.setCompleted(true);
        //toAdd.setResponse(new Feedback());

        System.out.println(toAdd);

        return historyRepository.save(toAdd).toView();
    }
}