package com.therame.controllers;

import com.therame.exception.EmptyMediaException;
import com.therame.exception.InvalidMediaException;
import com.therame.model.Exercise;
import com.therame.model.ExerciseForm;
import com.therame.service.MediaResolverService;
import com.therame.service.MediaStorageService;
import com.therame.view.ValidationErrorView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class MediaRestController {

    private MediaResolverService mediaResolverService;
    private MediaStorageService mediaStorageService;

    public MediaRestController(MediaResolverService mediaResolverService, MediaStorageService mediaStorageService) {
        this.mediaResolverService = mediaResolverService;
        this.mediaStorageService = mediaStorageService;
    }

    @GetMapping("/watch")
    public String videoView(@RequestParam("v") String videoId, Model model) {
        model.addAttribute("videoId", videoId);
        return "watch";
    }

    @GetMapping("/api/video")
    public void getVideo(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("path") String name) throws ServletException, IOException {
        request.setAttribute("video-request", name);
        mediaResolverService.handleRequest(request, response);
    }

    @GetMapping("/upload")
    public ModelAndView uploadView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exerciseForm", new ExerciseForm());
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadVideo(@Valid ExerciseForm exerciseForm, HttpServletRequest request) throws IOException {

        // Use local address for now, in the future (maybe, if we get to it) we'll want to use
        // the base address of the media server we store it on
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getLocalName(), request.getLocalPort());

        Exercise exercise = exerciseForm.toExercise();
        exercise.setMediaUrl(baseUrl);

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
