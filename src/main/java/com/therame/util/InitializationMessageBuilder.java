package com.therame.util;

import com.therame.model.User;
import org.springframework.mail.SimpleMailMessage;

public class InitializationMessageBuilder {

    public static SimpleMailMessage buildInitializationMessage(User user, String hostUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Welcome to TheraMe! Please activate your account.");
        message.setText("To activate your account, please click the link below:\n" +
                hostUrl + "/confirm?token=" + user.getInitCode() + "\n\n" +
                "If this is an error, please feel free to ignore this message.");
        message.setFrom("noreply@therame.com");

        return message;
    }

}
