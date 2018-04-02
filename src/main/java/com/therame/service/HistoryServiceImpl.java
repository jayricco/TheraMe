package com.therame.service;

import com.therame.model.*;
import com.therame.repository.jpa.*;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
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
                              UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<HistoryView> getHistoryForPatientId(UUID patientId){
        return historyRepository.findByPatientId(userRepository.findOne(patientId)).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryView> getForAllPatients(UUID therapistId){
        return historyRepository.findByTherapistId(userRepository.findOne(therapistId)).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackView> getFeedbackForPatientId(UUID patientId){
        return feedbackRepository.findByPatient(userRepository.getOne(patientId)).stream()
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

        if (user == null){
            throw new IllegalArgumentException("User not found!");
        }
        else if (exercise == null){
            throw new IllegalArgumentException("Exercise not found!");
        }
        else if (feedback == ""){
            throw new IllegalArgumentException("Feedback not provided!");
        }

        toAdd.setComments(feedback);

        return feedbackRepository.save(toAdd).toView();
    }

    @Override
    public List<HistoryView> getAllHistoryEntries() {
       return historyRepository.findAll().stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    //need to fix this one too
    @Override
    @Transactional
    public HistoryView addHistory(Assignment assignment, Time startTime, Time endTime){
        History toAdd = new History();
        toAdd.setAssignment(assignment);
        toAdd.setPatientId(assignment.getPatient());
        toAdd.setTherapistId(assignment.getTherapist());
        toAdd.setTimeEnd(startTime);
        toAdd.setTimeEnd(endTime);

        return historyRepository.save(toAdd).toView();
    }
}
