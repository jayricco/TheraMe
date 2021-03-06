package com.therame.model;

import com.therame.util.Base64Converter;
import com.therame.view.AssignmentView;
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
    private User patient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @CreationTimestamp
    @Column(name = "date_assigned")
    private Date dateAssigned;

    @Column(name = "ordering")
    private int order;

    public AssignmentView toView() {
        AssignmentView view = new AssignmentView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setExercise(exercise.toView());
        view.setDateAssigned(dateAssigned);
        view.setOrder(order);
        return view;
    }
}
