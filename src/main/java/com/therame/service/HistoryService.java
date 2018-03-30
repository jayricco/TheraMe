package com.therame.service;

import com.therame.model.Exercise;
import com.therame.view.HistoryView;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryService {

    List<HistoryView> getHistoryForPatientId();

    List<HistoryView> getForAllPatients();

    HistoryView addFeedback(UUID patientId, UUID exerciseId, String feedback);

    HistoryView createHistory(UUID patientId, UUID exerciseId, Date date);

}
