package com.therame.view;

import com.therame.model.User;
import lombok.Data;

@Data
public class UserView {

    private String id;
    private String firstName;
    private String lastName;
    private User.Type type;
    private UserView therapist;
    private boolean active;

}
