package com.therame.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "provider")
public class Provider {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "address", nullable = false)
    private String address;

    @NotEmpty
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "hours", nullable = false)
    private String hours;

}
