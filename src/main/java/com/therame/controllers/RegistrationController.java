package com.therame.controllers;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.therame.model.User;
import com.therame.service.EmailService;
import com.therame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegistrationController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user,
        BindingResult bindingResult, HttpServletRequest request) {
        //lookup by email
        Optional<User> userOptional = userService.findUserByEmail(user.getEmail());
        System.out.println(userOptional.toString());

        if(userOptional.isPresent()) {
            modelAndView.addObject("errorMessage", "There is already a user with that email!");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }

        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        }
        else {
            //case of new user, never before seen
            user.setEnabled(false);
            user.setActive(false);

            user.setConfirmationToken(UUID.randomUUID().toString());

            userService.createRootUser(user);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Welcome To TheraMe! Account Registration Confirmation");
            registrationEmail.setText("To initialize your account and set your password, please click the link below:\n"
                                + baseUrl + "/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@therame.com");

            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("confirmationMessage", "A confirmation email has been sent to " + user.getEmail());
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }
    // Set up a point to process confirmation
    @RequestMapping(value="/confirm", method = RequestMethod.GET)
    public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {
        Optional<User> userOptional = userService.findByConfirmationToken(token);

        if(userOptional.isPresent())
        {
            modelAndView.addObject("confirmationToken", userOptional.get().getConfirmationToken());
        }
        else {
            modelAndView.addObject("invalidToken", "This is an invalid link...?");
        }
        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    // Process confirmation link.
    @RequestMapping(value="/confirm", method = RequestMethod.POST)
    public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult,
                                                @RequestParam Map<String, String> requestParams, RedirectAttributes redirect) {
        modelAndView.setViewName("confirm");

        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(requestParams.get("password"));
        if (strength.getScore() < 2) {
            bindingResult.reject("password");
            redirect.addFlashAttribute("errorMessage", "Your password is too weak.");

            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }
        if (!requestParams.get("password").contentEquals(requestParams.get("passwordConfirm")))
        {
            bindingResult.reject("password");
            bindingResult.reject("passwordConfirm");
            redirect.addFlashAttribute("errorMessage", "Your entries for new password confirmation do not match!");
            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }

        Optional<User> userOptional = userService.findByConfirmationToken(requestParams.get("token"));
        if (!userOptional.isPresent())
        {
            throw new RuntimeException("I don't know what's happening");
        }
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(requestParams.get("password")));
        user.setActive(true);
        user.setEnabled(true);
        userService.saveUser(user);
        modelAndView.addObject("successMessage", "Your password has been set successfully!");
        return modelAndView;
    }
}
