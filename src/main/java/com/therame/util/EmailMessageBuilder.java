package com.therame.util;

import com.therame.model.User;
import org.springframework.mail.SimpleMailMessage;

public class EmailMessageBuilder {

    public static SimpleMailMessage buildInitializationMessage(User user, String hostUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Welcome to TheraMe! Please activate your account.");
        message.setText("To activate your account, please click the link below:\n" +
                hostUrl + "/confirm?token=" + user.getConfirmationToken() + "\n\n" +
                "If this is an error, please feel free to ignore this message.");
        message.setFrom("noreply@therame.com");

        return message;
    }

    public static SimpleMailMessage buildResetMessage(User user, String hostUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("TheraMe Password Reset");
        message.setText("To reset your password, please click the link below:\n" +
                hostUrl + "/confirm?token=" + user.getConfirmationToken() + "\n\n" +
                "If this is an error, please feel free to ignore this message.");
        message.setFrom("noreply@therame.com");

        return message;
    }

}
