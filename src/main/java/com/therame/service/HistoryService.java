package com.therame.service;

import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import com.therame.view.ReportView;

import java.util.List;
import java.util.UUID;

public interface HistoryService {

    ReportView getHistoryForPatientId(UUID patientId);

    List<ReportView> getForAllPatients(UUID therapistId);

    FeedbackView addFeedback(UUID patientId, UUID exerciseId, String feedback);

    HistoryView addHistory(UUID patientId, UUID assignmentId);

    List<FeedbackView> getFeedbackForPatientId(UUID patientId);

}
