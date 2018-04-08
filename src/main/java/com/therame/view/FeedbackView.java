package com.therame.view;

import lombok.Data;

import java.sql.Time;

@Data
public class FeedbackView {

    private String id;
    private String attemptId;
    private Time timestamp;
    private Time videoTime;
    private String comments;
    private UserView patient;
    private AssignmentView assignment;
    private HistoryView history;
}
