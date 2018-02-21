package com.therame.controllers;

import com.therame.model.DetailedUserDetails;
import com.therame.service.AssignmentService;
import com.therame.util.Base64Converter;
import com.therame.view.AssignmentView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssignmentController {

    private AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/api/assignments")
    public ResponseEntity<?> getAssignmentsForCurrentUser(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        List<AssignmentView> assignments = assignmentService.getForPatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/api/assignments/add")
    public ResponseEntity<?> addAssignmentForUser(@RequestParam("userId") String userId, @RequestParam("exerciseId") String exerciseId,
                                                  @RequestParam(value = "order", required = false) Integer order) {
        AssignmentView createdAssignment = assignmentService.createAssignment(Base64Converter.fromUrlSafeString(userId),
                Base64Converter.fromUrlSafeString(exerciseId), order != null ? order : 0);
        return ResponseEntity.ok(createdAssignment);
    }
}
