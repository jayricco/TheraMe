package com.therame.repository.jpa;

import com.therame.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("assignmentRepo")
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByPatientId(UUID patientId);

    List<Assignment> findByTherapistId(UUID therapistId);

}
