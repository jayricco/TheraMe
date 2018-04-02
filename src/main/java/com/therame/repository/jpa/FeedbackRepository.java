package com.therame.repository.jpa;

import com.therame.model.Feedback;
import com.therame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("feedbackRepo")
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> findByPatient(User patient);

}
