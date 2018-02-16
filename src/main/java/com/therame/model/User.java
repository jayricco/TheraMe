package com.therame.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "therame_user")
public class User implements Serializable {

    public enum Type {
        PATIENT,
        THERAPIST,
        ADMIN
    }

    // Model initialization
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Please provide a valid email address")
    @NotEmpty(message = "At least attempt to get it right...")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "Please provide your first name ")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Please provide your last name")
    private String lastName;

    @Column(name = "password", nullable = false)
    @Length(min = 5, message="*Your password must have at least 5 characters!")
    @Transient
    private String password;

    @Column(name = "type", columnDefinition = "smallint", nullable = false)
    @NotNull
    @Enumerated
    private Type type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
