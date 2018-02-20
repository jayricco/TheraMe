package com.therame.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    User patient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    Exercise exercise;

    @CreationTimestamp
    @Column(name = "date_assigned")
    Date dateAssigned;

    @Column(name = "order")
    int order;
}
