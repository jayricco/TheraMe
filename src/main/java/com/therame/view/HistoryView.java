package com.therame.view;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class HistoryView {

    private String id;
    private AssignmentView assignmentId;
    private UserView patientId;
    private Date timeStart;
    private Date timeEnd;
    private boolean completed;
    private FeedbackView responseId;
}
