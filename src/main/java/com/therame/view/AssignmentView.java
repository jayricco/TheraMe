package com.therame.view;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class AssignmentView {

    private String id;
    private ExerciseView exercise;
    private Date dateAssigned;
    private int order;
    private String last_completed;
}
