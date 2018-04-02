package com.therame.view;

import com.therame.model.Assignment;
import com.therame.model.User;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Data
public class HistoryView {

    private String id;
    private String patientId;
    private String therapistId;
    private String assignmentId;
    private Time timeStart;
    private Time timeEnd;
    private boolean completed;
    private String responseId;
}
