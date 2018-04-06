package com.therame.service;

import com.therame.view.AssignmentView;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    List<AssignmentView> getForPatientId(UUID patientId);

    List<AssignmentView> getIncompleteForPatientId(UUID patientId);

    AssignmentView createAssignment(UUID patientId, UUID exerciseId, int order);

    void deleteAssignment(UUID assignmentId);

}
