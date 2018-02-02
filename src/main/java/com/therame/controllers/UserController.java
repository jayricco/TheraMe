package com.therame.controllers;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        boolean isTherapist = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN")
                || authority.getAuthority().equals("THERAPIST"));

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

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        // Only create user if valid
        if (!bindingResult.hasErrors()) {
            try {
                userService.createUser(user);
                modelAndView.addObject("successMessage", "User has been registered successfully");
                modelAndView.addObject("user", new User());
            } catch (DataIntegrityViolationException e) {
                // We assume the cause is the 'email' field here, maybe in the future we should verify this to be true
                bindingResult.rejectValue("email", "error.user", "Email is already in use.");
            }
        }

        modelAndView.setViewName("registration");
        return modelAndView;
    }
}
