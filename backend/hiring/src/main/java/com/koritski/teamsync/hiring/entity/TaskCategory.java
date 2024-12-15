package com.koritski.teamsync.hiring.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task_category", schema = "hiring")
@Data
@Accessors(chain = true)
public class TaskCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "organization_id")
    private Long organizationId;

    @OneToMany(mappedBy = "taskCategory", cascade = CascadeType.PERSIST)
    private List<InterviewTask> interviewTasks;
}
