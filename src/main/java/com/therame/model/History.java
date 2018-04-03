package com.therame.model;

import com.therame.util.Base64Converter;
import com.therame.view.AssignmentView;
import com.therame.view.HistoryView;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "history")
public class History {

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
    @JoinColumn(name = "assignment_id")
    private Assignment assignmentId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patientId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private User therapistId;

    @NotNull
    @Column(name = "time_start")
    private Time timeStart;

    @NotNull
    @Column(name = "time_end")
    private Time timeEnd;

    @NotNull
    @Column(name = "completed")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private History responseId;

    public HistoryView toView() {
        HistoryView view = new HistoryView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setAssignmentId(assignmentId.toView());
        view.setTimeStart(timeStart);
        view.setTimeEnd(timeEnd);
        view.setCompleted(completed);
        view.setResponseId(responseId.toView());
        return view;
    }
}
