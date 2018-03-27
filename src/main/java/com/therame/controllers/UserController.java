package com.therame.controllers;

import javax.validation.Valid;

import com.google.common.collect.ImmutableList;
import com.therame.model.DetailedUserDetails;
import com.therame.util.Base64Converter;
import com.therame.view.UserView;
import com.therame.view.ValidationErrorView;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.therame.model.User;
import com.therame.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String home(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        if (userDetails.getUser().getType() == User.Type.THERAPIST || userDetails.getUser().getType() == User.Type.ADMIN) {
            return "pt_home";
        } else {
            return "home";
        }
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@Valid User user, @AuthenticationPrincipal DetailedUserDetails userDetails) {
        try {
            if (user.getType() == User.Type.PATIENT && user.getTherapist() == null) {

                // Set current user as PT if we're adding a patient and none was specified
                user.setTherapist(userDetails.getUser());
            }

            // If we're not the sentinel user, set it to the current user's provider
            if (userDetails.getUser().getType() != User.Type.ADMIN || userDetails.getUser().getProvider() != null) {
                user.setProvider(userDetails.getUser().getProvider());
            }

            User createdUser = userService.createUser(user);
            createdUser.setPassword(null);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // We assume the cause is the 'email' field here, maybe in the future we should verify this to be true
            ValidationErrorView errorView = new ValidationErrorView();
            errorView.addError(new FieldError("user", "email", "Email is already in use."));
            return new ResponseEntity<>(errorView, HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/users")
    public String usersView() {
        return "users";
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "q", required = false) String nameQuery,
            @RequestParam(value = "types", required = false) List<User.Type> typeFilters) {
        if (typeFilters == null) {
            typeFilters = ImmutableList.of(User.Type.ADMIN, User.Type.THERAPIST, User.Type.PATIENT);
        }

        if (nameQuery == null) {
            nameQuery = "";
        }

        return ResponseEntity.ok(userService.findAllUsersByNameAndType(nameQuery, typeFilters));
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
    @GetMapping("/user")
    public ModelAndView userView(@RequestParam("id") String userId) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<UserView> optionalUser = userService.findById(Base64Converter.fromUrlSafeString(userId));

        optionalUser.ifPresent(user -> {
            modelAndView.addObject("forUser", user);
            modelAndView.setViewName("user");
        });

        return modelAndView;
    }
}
