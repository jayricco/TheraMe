package com.therame.model;

import com.therame.view.ExerciseView;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @URL
    @NotEmpty
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @NotEmpty
    @Column(name = "run_time", nullable = false)
    private String runTime;

    @Column(name = "tag_set_id")
    private UUID tagSetId;

    public ExerciseView toView() {
        ExerciseView view = new ExerciseView();
        view.setId(Base64Utils.encodeToUrlSafeString(id.toString().getBytes()));
        view.setTitle(title);
        view.setDescription(description);
        view.setMediaUrl(mediaUrl);
        view.setRunTime(runTime);
        return view;
    }

}
