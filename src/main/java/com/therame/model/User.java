package com.therame.model;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "therame_user")
public class User {

    public enum Type {
        PATIENT,
        THERAPIST,
        ADMIN
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email is required.")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "First name is required.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "Last name is required.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Transient
    @Length(min = 5, message="Password must contain over 5 characters.")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "permission_level", columnDefinition = "smallint", nullable = false)
    @NotNull
    @Enumerated
    private Type type;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "pt_id")
    private User therapist;

    @Nullable
    @Column(name = "init_code")
    private String init_code;


    // These columns and callbacks are for monitoring some temporal data
    // Just keeping track of the date of creation - and then the last time of update.

    @Column(name = "date_created")
    private Date date_created;

    @Column(name = "last_updated")
    private Date last_updated;

    // As well, in here is where the init code is going to go!
    @PrePersist
    protected void onCreate() {
        date_created = new Date();
        if (password == null) {
            init_code = StringUtils.randomAlphanumeric(6);
            password = init_code;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        last_updated = new Date();
    }
}
