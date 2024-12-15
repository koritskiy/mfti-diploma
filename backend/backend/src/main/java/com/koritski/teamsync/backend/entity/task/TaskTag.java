package com.koritski.teamsync.backend.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "task_tag", schema = "tracker")
@Data
@Accessors(chain = true)
public class TaskTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_value")
    private String tagValue;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @JsonIgnore
    private Tag tag;
}
