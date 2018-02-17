package com.therame.controllers;

import javax.validation.Valid;

import com.therame.view.ValidationErrorView;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.therame.model.User;
import com.therame.service.UserService;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/")
    public ModelAndView home(Authentication auth) {
        ModelAndView modelAndView = new ModelAndView();
        boolean isTherapist = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(User.Type.ADMIN.name())
                || authority.getAuthority().equals(User.Type.THERAPIST.name()));

        if (isTherapist) {
            modelAndView.setViewName("pt_home");
        } else {
            modelAndView.setViewName("home");
        }

        return modelAndView;
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

    @ResponseBody
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@Valid User user) {
        try {
            User createdUser = userService.createRootUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // We assume the cause is the 'email' field here, maybe in the future we should verify this to be true
            ValidationErrorView errorView = new ValidationErrorView();
            errorView.addError(new FieldError("user", "email", "Email is already in use."));
            return new ResponseEntity<>(errorView, HttpStatus.CONFLICT);
        }
    }
}
