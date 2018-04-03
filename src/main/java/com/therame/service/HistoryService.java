package com.therame.service;

import com.therame.model.Exercise;
import com.therame.model.Feedback;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryService {

    List<HistoryView> getHistoryForCurrentUser(UUID patientId);

    List<HistoryView> getHistoryForPatientId(UUID patientId);

    List<HistoryView> getForAllPatients(UUID therapistId);

    FeedbackView addFeedback(UUID patientId, UUID exerciseId, String feedback);

    HistoryView addHistory(UUID patientId, UUID assignmentId, Time startTime, Time endTime);

    List<FeedbackView> getFeedbackForPatientId(UUID patientId);

}
