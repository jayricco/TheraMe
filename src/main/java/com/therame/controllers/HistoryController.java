package com.therame.controllers;

import com.therame.model.DetailedUserDetails;
import com.therame.service.HistoryService;
import com.therame.util.Base64Converter;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import com.therame.view.ReportView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {
    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/history/currentuser")
    public ResponseEntity<?> getHistoryForCurrentUser(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        ReportView history = historyService.getHistoryForPatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(history);
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/api/history/allpatients")
    public ResponseEntity<?> getHistoryForAllPatients(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        List<ReportView> history = historyService.getForAllPatients(userDetails.getUser().getId());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/api/history/specificpatient")
    public ResponseEntity<?> getHistoryForPatientId(@RequestParam("id") String userId) {
        ReportView history = historyService.getHistoryForPatientId(Base64Converter.fromUrlSafeString(userId));
        return ResponseEntity.ok(history);
    }

    @PostMapping("/api/history/add")
    public ResponseEntity<?> addHistoryForUser(@RequestParam("assignmentId") String assignmentId, @AuthenticationPrincipal DetailedUserDetails userDetails) {
        HistoryView addedHistory = historyService.addHistory(userDetails.getUser().getId(), Base64Converter.fromUrlSafeString(assignmentId));
        return ResponseEntity.ok(addedHistory);
    }

    @PostMapping("/api/history/feedbackAdd")
    public ResponseEntity<?> addFeedbackForExercise(@RequestParam("exerciseId") String exerciseId, @RequestParam("feedback") String feedback,
                                                    @AuthenticationPrincipal DetailedUserDetails userDetails) {
        FeedbackView addedFeedback = historyService.addFeedback(userDetails.getUser().getId(), Base64Converter.fromUrlSafeString(exerciseId), feedback);
        return ResponseEntity.ok(addedFeedback);
    }

    @GetMapping("/api/history/feedback")
    public ResponseEntity<?> getFeedbackForPatientId(@RequestParam("patient_id") String patientId){
        List<FeedbackView> feedback = historyService.getFeedbackForPatientId(Base64Converter.fromUrlSafeString(patientId));
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/reports")
    public String viewReports() {
        return "reports";
    }


}