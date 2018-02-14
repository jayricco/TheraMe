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
}
