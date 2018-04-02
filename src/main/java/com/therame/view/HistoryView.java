package com.therame.view;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class HistoryView {

    private String id;
    private String assignmentId;
    private Time timeStart;
    private Time timeEnd;
    private boolean completed;
    private String responseId;
}
