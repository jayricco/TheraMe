package com.therame.controllers;

import com.therame.exception.EmptyMediaException;
import com.therame.exception.InvalidMediaException;
import com.therame.model.Exercise;
import com.therame.model.ExerciseForm;
import com.therame.service.ExerciseService;
import com.therame.service.MediaResolverService;
import com.therame.service.MediaStorageService;
import com.therame.view.ValidationErrorView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class MediaRestController {

    @Value("${therame.media.host.url}")
    private String mediaHostUrl;

    private MediaResolverService mediaResolverService;
    private MediaStorageService mediaStorageService;
    private ExerciseService exerciseService;

    public MediaRestController(MediaResolverService mediaResolverService, MediaStorageService mediaStorageService,
                               ExerciseService exerciseService) {
        this.mediaResolverService = mediaResolverService;
        this.mediaStorageService = mediaStorageService;
        this.exerciseService = exerciseService;
    }

    @GetMapping(path = "/api/video", produces = "video/mp4")
    public void getVideo(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("id") String id) throws ServletException, IOException {
        request.setAttribute("resource-name", id);
        request.setAttribute("resource-type", "video");

        System.out.println("Grabbing video with id: " + id);
        mediaResolverService.handleRequest(request, response);
    }

    @GetMapping("/api/thumbnail")
    public void getThumbnail(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("id") String id) throws ServletException, IOException {
        request.setAttribute("resource-name", id);
        request.setAttribute("resource-type", "image");

        try {
            mediaResolverService.handleRequest(request, response);
        } catch(FileNotFoundException e) {
            request.setAttribute("resource-name", "default-thumbnail");
            mediaResolverService.handleRequest(request, response);
        }
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/upload")
    public ModelAndView uploadView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exerciseForm", new ExerciseForm());
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @ResponseBody
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadVideo(@Valid ExerciseForm exerciseForm) throws IOException {
        Exercise exercise = exerciseForm.toExercise();
        if (exerciseService.findByTitle(exerciseForm.getTitle()) != null) {
            ValidationErrorView errorView = new ValidationErrorView();
            errorView.addError(new FieldError("exerciseForm", "title", "An exercise with that title already exists!"));
            return new ResponseEntity<>(errorView, HttpStatus.BAD_REQUEST);
        }

        exercise.setMediaUrl(mediaHostUrl);

        try {
            Exercise createdExercise = mediaStorageService.store(exercise, exerciseForm.getVideoFile());
            return ResponseEntity.ok(createdExercise);
        }  catch (EmptyMediaException | InvalidMediaException e) {
            ValidationErrorView errorView = new ValidationErrorView();
            errorView.addError(new FieldError("exerciseForm", "videoFile", e.getMessage()));
            return new ResponseEntity<>(errorView, HttpStatus.BAD_REQUEST);
        }
    }
}
