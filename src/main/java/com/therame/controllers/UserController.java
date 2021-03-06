package com.therame.controllers;

import javax.validation.Valid;

import com.google.common.collect.ImmutableList;
import com.therame.exception.ResourceNotFoundException;
import com.therame.model.DetailedUserDetails;
import com.therame.model.UserRepository;
import com.therame.service.AssignmentService;
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
    private AssignmentService assignmentService;

    public UserController(UserService userService, AssignmentService assignmentService) {
        this.userService = userService;
        this.assignmentService = assignmentService;
    }

    @GetMapping(value = "/api/checkauth")
    public ResponseEntity<?> checkAuthentication(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        if (userDetails.isEnabled()) {
            return ResponseEntity.ok(userDetails.getUser().toView());
        } else {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("You bad.");
        }
    }

    @GetMapping(value = "/")
    public ModelAndView home(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView();

        if (userDetails.getUser().getType() == User.Type.THERAPIST || userDetails.getUser().getType() == User.Type.ADMIN) {
            modelAndView.setViewName("pt_home");
        } else {
            int numIncomplete = assignmentService.getIncompleteForPatientId(userDetails.getUser().getId()).size();

            modelAndView.addObject("numIncomplete", numIncomplete);
            modelAndView.setViewName("home");
        }

        return modelAndView;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value="/updateInfo", method=RequestMethod.GET)
    public ModelAndView updateInfo(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("updateInfo");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('THERAPIST', 'ADMIN')")
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

    @GetMapping("/confirm")
    public String updatePasswordView(@RequestParam("token") String code) {
        Optional<User> forUser = userService.findUserByConfirmationToken(code);

        if (forUser.isPresent()) {
            return "initialize_account";
        } else {
            throw new ResourceNotFoundException("Invalid confirmation token.");
        }
    }

    @PostMapping("/api/confirm")
    public ResponseEntity<?> updatePassword(@RequestParam("token") String code, @RequestParam("password") String password) {
        Optional<User> user = userService.updatePasswordForConfirmationToken(code, password);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().toView());
        } else {
            throw new ResourceNotFoundException("Invalid confirmation token.");
        }

    }

    @GetMapping("/resetPassword")
    public String resetPasswordView() {
        return "password_reset";
    }

    @PostMapping("/api/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        userService.sendPasswordResetEmail(email);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/setactive", method = RequestMethod.POST)
    public ResponseEntity<?> setActiveState(@RequestParam("id") String userId, @RequestParam("active") Boolean active,
                                            @AuthenticationPrincipal DetailedUserDetails userDetails) {
        User deactivatedUser = userService.updateActiveStatus(Base64Converter.fromUrlSafeString(userId), active);
        return ResponseEntity.ok(deactivatedUser);
    }

    @RequestMapping(value = "/api/change", method = RequestMethod.POST)
    public ResponseEntity<?> updateUserInfo(User user, @AuthenticationPrincipal DetailedUserDetails userDetails){
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            userDetails.getUser().setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null && !user.getLastName().isEmpty()) {
            userDetails.getUser().setLastName(user.getLastName());
        }

        User updatedUser = userService.updateUser(userDetails.getUser());
        return ResponseEntity.ok(updatedUser);
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
            typeFilters = ImmutableList.of(User.Type.PATIENT);
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

        if (optionalUser.isPresent()) {
            modelAndView.addObject("forUser", optionalUser.get());
            modelAndView.setViewName("user");
        } else {
            throw new ResourceNotFoundException("User not found!");
        }

        return modelAndView;
    }
}
