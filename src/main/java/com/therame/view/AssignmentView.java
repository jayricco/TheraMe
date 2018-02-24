package com.therame.view;

import lombok.Data;

import java.util.Date;

@Data
public class AssignmentView {

    private String id;
    private ExerciseView exercise;
    private Date dateAssigned;
    private int order;

}
