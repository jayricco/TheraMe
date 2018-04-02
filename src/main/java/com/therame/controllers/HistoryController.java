package com.therame.controllers;

import com.therame.model.DetailedUserDetails;
import com.therame.service.HistoryService;
import com.therame.util.Base64Converter;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Controller
public class HistoryController {
    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/history")
    public ResponseEntity<?> getHistoryForCurrentUser(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        List<HistoryView> history = historyService.getHistoryForPatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(history);
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("api/history")
    public ResponseEntity<?> getHistoryForAllPatients(@RequestParam("therapistId") String therapistId){
        List<HistoryView> history = historyService.getForAllPatients(Base64Converter.fromUrlSafeString(therapistId));
        return ResponseEntity.ok(history);
    }

    @GetMapping("api/history")
    public ResponseEntity<?> getHistoryForPatientId(@RequestParam("id") String userId){
        List<HistoryView> history = historyService.getHistoryForPatientId(Base64Converter.fromUrlSafeString(userId));
        return ResponseEntity.ok(history);
    }

    @PostMapping("/api/history/add")
    public ResponseEntity<?> addHistoryForUser(@RequestParam("userId") String userId, @RequestParam("exerciseId") String exerciseId,
                                               Time startTime, Time endTime) {
        HistoryView addedHistory = historyService.addHistory(Base64Converter.fromUrlSafeString(userId), Base64Converter.fromUrlSafeString(exerciseId), startTime, endTime);
        return ResponseEntity.ok(addedHistory);
    }

    @PostMapping("/api/history/feedbackAdd")
    public ResponseEntity<?> addFeedbackForExercise(@RequestParam("userId") String userId, @RequestParam("exerciseId") String exerciseId,
                                                    String feedback){
        FeedbackView addedFeedback = historyService.addFeedback(Base64Converter.fromUrlSafeString(userId), Base64Converter.fromUrlSafeString(exerciseId), feedback);
        return ResponseEntity.ok(addedFeedback);
    }


}