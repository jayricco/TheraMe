package com.therame.service;

import com.therame.model.Assignment;
import com.therame.view.AssignmentView;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    Assignment getAssignmentById(UUID assignmentId);

    List<AssignmentView> getForPatientId(UUID patientId);

    AssignmentView createAssignment(UUID patientId, UUID exerciseId, int order);

    void deleteAssignment(UUID assignmentId);

}
