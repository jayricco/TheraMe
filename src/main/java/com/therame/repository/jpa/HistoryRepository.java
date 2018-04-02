package com.therame.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("historyRepo")
public interface HistoryRepository extends JpaRepository<History, UUID> {

    List<History> findByPatientId(UUID patientId);

    List<History> findByTherapistId(UUID therapistId);

}
