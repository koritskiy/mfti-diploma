package com.koritski.teamsync.hiring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "interview_task_stack", schema = "hiring")
@Data
@Accessors(chain = true)
public class InterviewTaskStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "task_id")
    @ManyToOne
    private InterviewTask interviewTask;

    @JsonIgnore
    @JoinColumn(name = "interview_id")
    @ManyToOne
    private Interview interview;
}
