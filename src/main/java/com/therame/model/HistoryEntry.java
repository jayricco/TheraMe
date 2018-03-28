package com.therame.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.solr.core.mapping.Indexed;

import javax.persistence.*;
import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "assignment_history")
public class HistoryEntry {

    public enum EntryType {
        CompletionRecord,
        Feedback
    }
    public enum FeedbackType {
        EXERCISE_QUALITY,
        PAIN_EXPERIENCE,
        OTHER
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Indexed(name = "id", type="uuid")
    private UUID id;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private Date timestamp;

    @Column(name = "entry_type", nullable = false)
    @Enumerated
    private EntryType type;

    @Column(name = "feedback_type")
    @Enumerated
    private FeedbackType feedbackType;

    @Column(name = "message_data")
    private StringBuffer message;



}
