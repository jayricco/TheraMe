package com.therame.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Convenience class for accepting exercise form data
 */
@Data
public class ExerciseForm {

    @NotEmpty(message = "Title is required.")
    private String title;

    @NotEmpty(message = "Description is required.")
    private String description;

    @NotNull(message = "Video file is required.")
    private MultipartFile videoFile;

    public Exercise toExercise() {
        Exercise exercise = new Exercise();
        exercise.setTitle(title);
        exercise.setDescription(description);
        return exercise;
    }

}
