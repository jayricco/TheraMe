package com.therame.service;

import com.therame.model.*;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
                              UserRepository userRepository, HistoryRepository historyRepository,
                              FeedbackRepository feedbackRepository) {
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<HistoryView> getHistoryForCurrentUser(UUID patientId){
        return historyRepository.findByPatientId(patientId).stream()
                .map(History::toView)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryView> getHistoryForPatientId(UUID patientId){
        System.out.println("testing by patientId: "+patientId);
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
        return feedbackRepository.findByPatientId(userRepository.findOne(patientId)).stream()
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

    //need to fix this one too
    @Override
    @Transactional
    public HistoryView addHistory(UUID patientId, UUID assignmentId, Date startTime, Date endTime){
        Assignment assignment = assignmentRepository.findOne(assignmentId);
        History toAdd = new History();


        toAdd.setAssignment(assignment);
        toAdd.setTimeEnd(startTime);
        toAdd.setTimeEnd(endTime);

        return historyRepository.save(toAdd).toView();
    }
}
