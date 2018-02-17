package com.therame.view;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * (Probably) A temporary way to let the client know of detailed form validation errors. Pretty much just a wrapper
 * around a list so that we don't have to have specific code on the client side for Spring's BindException and our own.
 * There's definitely a better way to go about this, but this is easy...
 */
public class ValidationErrorView {

    @Getter
    private List<ObjectError> errors;

    public ValidationErrorView() {
        this.errors = new ArrayList<>();
    }

    public void addError(ObjectError error) {
        errors.add(error);
    }
}
