package com.therame.repository.jpa;

import com.therame.model.History;
import com.therame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("historyRepo")
public interface HistoryRepository extends JpaRepository<History, UUID> {

    List<History> findByPatientId(User patientId);

    List<History> findByTherapistId(User therapistId);

}
