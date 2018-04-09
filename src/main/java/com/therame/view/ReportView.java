package com.therame.view;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ReportView {

    private UserView user;
    private Map<LocalDate, Boolean> history;

}
