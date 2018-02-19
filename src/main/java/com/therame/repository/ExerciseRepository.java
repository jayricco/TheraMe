package com.therame.repository;

import com.therame.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("exerciseRepo")
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    @Query("select e from Exercise e where title like %:title%")
    List<Exercise> findAllByTitle(@Param("title") String title);

}
