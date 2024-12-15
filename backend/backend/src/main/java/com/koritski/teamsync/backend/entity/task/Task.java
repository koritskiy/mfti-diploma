package com.koritski.teamsync.backend.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.organization.Worker;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task", schema = "tracker")
@Data
@Accessors(chain = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @JoinColumn(name = "worker_id")
    @ManyToOne
    private Worker worker;

    @JoinColumn(name = "board_column_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private BoardColumn boardColumn;

    @OneToMany(mappedBy = "task", cascade = CascadeType.PERSIST)
    private List<TaskTag> taskTagList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.PERSIST)
    private List<GitHubBranch> branches;
}
