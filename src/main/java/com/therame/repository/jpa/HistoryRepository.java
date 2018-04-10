package com.therame.repository.jpa;

import com.therame.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository("historyRepo")
public interface HistoryRepository extends JpaRepository<History, UUID> {

    @Query("select h from History h where h.patient.id = :patientId AND h.timeStart >= (current_time)  ORDER BY h.timeStart")
    List<History> findByPatientId(@Param("patientId") UUID patientId);

    @Query("select h from History h where h.therapist.id = :therapistId AND h.timeStart >= (current_time ) ORDER BY h.patient.id, h.timeStart")
    List<History> findByTherapistId(@Param("therapistId") UUID therapistId );

}
