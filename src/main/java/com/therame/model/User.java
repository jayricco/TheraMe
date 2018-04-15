package com.therame.model;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.therame.util.Base64Converter;
import com.therame.view.UserView;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.sql.Timestamp;
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

    @Nullable
    @Transient
    @Column(name = "password")
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
    @Column(name = "confirmation_token")
    private String confirmationToken;

    @NotNull
    @Column(name = "active")
    private boolean active;

    @NotNull
    @Column(name = "enabled")
    private boolean enabled;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private Timestamp last_updated;

    public UserView toView() {
        UserView view = new UserView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setFirstName(firstName);
        view.setLastName(lastName);
        view.setType(type);
        view.setActive(active);

        if (therapist != null) {
            view.setTherapist(therapist.toView());
        }

        return view;
    }
    public String generateConfirmationToken() {
        String token = Base64Converter.toUrlSafeString(UUID.randomUUID());
        setConfirmationToken(token);
        return token;
    }
}
