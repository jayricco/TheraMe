package com.therame.controllers;

import com.therame.model.Assignment;
import com.therame.model.DetailedUserDetails;
import com.therame.model.Exercise;
import com.therame.model.User;
import com.therame.service.AssignmentService;
import com.therame.service.HistoryService;
import com.therame.util.Base64Converter;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class HistoryController {

    @Autowired
    private AssignmentService assignmentService;

    private HistoryService historyService;


    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/current_history")
    public ResponseEntity<?> getHistoryForCurrentUser(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        List<HistoryView> history = historyService.getHistoryForPatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(history);
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/api/history")
    public ResponseEntity<?> getHistoryForAllPatients(@RequestParam(name = "therapistid", required = false) String therapistId,
                                                      @RequestParam(name = "patientid", required = false) String userId){

        List<HistoryView> history = null;
        if(!therapistId.isEmpty()){
            history = historyService.getForAllPatients(Base64Converter.fromUrlSafeString(therapistId));
        } else if (!userId.isEmpty()) {
            history = historyService.getHistoryForPatientId(Base64Converter.fromUrlSafeString(userId));
        } else {
            history = historyService.getAllHistoryEntries();
        }

        return ResponseEntity.ok(history);
    }

    @PreAuthorize("hasAnyAuthority('PATIENT')")
    @PostMapping("/api/history/add")
    public ResponseEntity<?> addHistoryForUser(@RequestParam("assignmentid") String assignmentId,
                                               Time startTime, Time endTime) {

        HistoryView addedHistory = historyService.addHistory(assignmentService.getAssignmentById(
                Base64Converter.fromUrlSafeString(assignmentId)), startTime, endTime);
        return ResponseEntity.ok(addedHistory);
    }

    @PreAuthorize("hasAnyAuthority('PATIENT')")
    @PostMapping("/api/history/feedbackAdd")
    public ResponseEntity<?> addFeedbackForExercise(@RequestParam("userid") String userId, @RequestParam("exerciseid") String exerciseId,
                                                    String feedback){
        FeedbackView addedFeedback = historyService.addFeedback(Base64Converter.fromUrlSafeString(userId), Base64Converter.fromUrlSafeString(exerciseId), feedback);
        return ResponseEntity.ok(addedFeedback);
    }


}