package com.therame.controllers;

import com.therame.model.Exercise;
import com.therame.service.ExerciseService;
import com.therame.util.Base64Converter;
import com.therame.view.ExerciseView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ExerciseController {

    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/watch")
    public String exerciseViewById(@RequestParam("v") String encodedId, Model model) {

        UUID videoId = Base64Converter.fromUrlSafeString(encodedId);

        Optional<Exercise> optionalExercise = exerciseService.findExerciseById(videoId);
        optionalExercise.ifPresent((exercise) -> model.addAttribute("exercise", exercise.toView()));

        return "watch";
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/videos")
    public String exerciseVideoListView() {
        return "videos";
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/api/videos")
    public ResponseEntity<?> getAllExerciseOrByQuery(@RequestParam(value = "q", required = false) String query) {
        List<Exercise> exercises = null;

        if (query != null) {
            try {
                exercises = exerciseService.searchExercisesByTitle(query);
                System.out.println(exercises);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            exercises = exerciseService.findAllExercises();
        }
        if (exercises == null) {
            return ResponseEntity.badRequest().body(query);
        }
        List<ExerciseView> views = exercises.stream()
                .map(Exercise::toView)
                .collect(Collectors.toList());

        return ResponseEntity.ok(views);
    }

}
