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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private UUID assignmentId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "time_start")
    private Time timeStart;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "time_end")
    private Time timeEnd;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "completed")
    private boolean completed;

    @Column(name = "response_id")
    private UUID responseId;

    //fix this
    public HistoryView toView() {
        HistoryView view = new HistoryView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setAssignmentId(Base64Converter.toUrlSafeString(assignmentId));
        view.setStartTime(timeStart);
        view.setEndTime(timeEnd);
        view.setCompleted(completed);
        view.setResponseId(Base64Converter.toUrlSafeString(responseId));
        return view;
    }
}
