package com.therame.repository.jpa;

import com.therame.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByPatientId(UUID patientId);

    @Query("select a from Assignment a where a.patient.id = :patientId and a.id not in " +
            "(select h.assignment.id from History h where h.timeEnd >= current_time and h.patient.id = :patientId)" +
            "order by a.order asc")
    List<Assignment> findIncompleteByPatientId(@Param("patientId") UUID patientId);

}
