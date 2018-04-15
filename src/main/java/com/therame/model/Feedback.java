package com.therame.model;

import com.therame.util.Base64Converter;
import com.therame.view.FeedbackView;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="patient_id")
    private User patient;

    @CreationTimestamp
    @Column(name = "timestamp")
    private Timestamp created;

    @NotNull
    @Column(name = "patient_comment")
    private String comments;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    public FeedbackView toView() {
        FeedbackView view = new FeedbackView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setTimestamp(created);
        view.setComments(comments);
        view.setPatient(patient.toView());
        view.setExercise(exercise.toView());
        return view;
    }
}
