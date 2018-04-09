package com.therame.view;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class FeedbackView {

    private String id;
    private Date timestamp;
    private String comments;
    private UserView patient;
    private ExerciseView exercise;
}
