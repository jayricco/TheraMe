package com.therame.model;

import com.therame.util.Base64Converter;
import com.therame.view.FeedbackView;
import com.therame.view.HistoryView;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Time;
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
    @Column(name = "attempt_id")
    private UUID attemptId;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="patient_id")
    private User patient;

    @NotNull
    @Column(name = "timestamp")
    private Time timestamp;

    @NotNull
    @Column(name = "video_timecode")
    private Time videoTime;

    @NotNull
    @Column(name = "patient_comment")
    private String comments;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "pt_id")
    private User user;


    //fix this
    public FeedbackView toView() {
        FeedbackView view = new FeedbackView();
        view.setId(Base64Converter.toUrlSafeString(id));
        view.setAttemptId(Base64Converter.toUrlSafeString(attemptId));
        view.setTimestamp(timestamp);
        view.setVideoTime(videoTime);
        view.setComments(comments);
        return view;
    }
}
