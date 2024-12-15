package com.koritski.teamsync.hiring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "interview_task", schema = "hiring")
@Data
@Accessors(chain = true)
public class InterviewTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "author")
    private String author;

    @JsonIgnore
    @JoinColumn(name = "task_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskCategory taskCategory;

    @OneToMany(mappedBy = "interviewTask", cascade = CascadeType.PERSIST)
    private List<InterviewTaskStack> interviewTaskStack;
}
