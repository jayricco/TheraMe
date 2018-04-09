package com.therame.service;

import com.therame.model.*;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import com.therame.view.ReportView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
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
    @Transactional
    public ReportView getHistoryForPatientId(UUID patientId) {

        // This is bad and I feel bad for writing it
        User patient = Objects.requireNonNull(userRepository.findOne(patientId));
        Map<LocalDate, Boolean> history = new HashMap<>(7);

        for (int i = 0; i < 7; i++) {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            date.setDate(date.getDate() - i);

            List<Assignment> missedAssignments = assignmentRepository.findIncompleteByPatientIdAndDate(patientId, date);
            history.put(date.toLocalDate(), missedAssignments.isEmpty());
        }

        ReportView reportView = new ReportView();
        reportView.setUser(patient.toView());
        reportView.setHistory(history);
        return reportView;
    }

    @Override
    @Transactional
    public List<ReportView> getForAllPatients(UUID therapistId) {

        // This is even worse and I feel even worse for writing it
        List<User> patients = userRepository.findAllByTherapistId(therapistId);
        List<ReportView> reports = new ArrayList<>(patients.size());

        patients.forEach(patient -> reports.add(getHistoryForPatientId(patient.getId())));
        return reports;
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

        return historyRepository.save(toAdd).toView();
    }
}
