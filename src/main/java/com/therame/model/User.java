package com.therame.model;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.therame.util.Base64Converter;
import com.therame.view.UserView;
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

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email is required.")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "First name is required.")

    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Last name is required.")
    private String lastName;

    @Column(name = "password")
    @Transient
    private String password;

    @Column(name = "permission_level", columnDefinition = "smallint", nullable = false)
    @Enumerated
    private Type type;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_id")
    private User therapist;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "active")
    private boolean active;

    @Column(name = "confirmation_token")
    private String confirmationToken;


    // These columns and callbacks are for monitoring some temporal data
    // Just keeping track of the date of creation - and then the last time of update.

    @Column(name = "date_created")
    private Date date_created;

    @Column(name = "last_updated")
    private Date last_updated;

    @PrePersist
    protected void onCreate() { date_created = new Date(); }

    @PreUpdate
    protected void onUpdate() {
        last_updated = new Date();
    }

    public UserView toView() {
        UserView view = new UserView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setFirstName(firstName);
        view.setLastName(lastName);
        view.setType(type);

        if (therapist != null) {
            view.setTherapist(therapist.toView());
        }

        return view;
    }
}
