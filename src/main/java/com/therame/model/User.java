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

import java.util.UUID;

@Data
@Entity
@Table(name = "`user`")
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
    @Length(min = 5, message="Your password must have at least 5 characters!")
    @Transient
    @NotNull
    private String password;

    @Column(name = "permission_level", columnDefinition = "smallint", nullable = false)
    @NotNull
    @Enumerated
    private Type type;

    @Nullable
    @Column(name = "pt_id")
    private UUID therapistId;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Nullable
    @Column(name = "init_code")
    private String init_code;
}
