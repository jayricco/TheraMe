package com.therame.repository;

import com.therame.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("exerciseRepo")
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

}
