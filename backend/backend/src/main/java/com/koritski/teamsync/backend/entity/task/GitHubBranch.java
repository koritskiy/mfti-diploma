package com.koritski.teamsync.backend.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "github_branch", schema = "tracker")
@Data
@Accessors(chain = true)
public class GitHubBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repo_full_name")
    private String repoFullName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "ref")
    private String ref;

    @JoinColumn(name = "task_id")
    @ManyToOne
    @JsonIgnore
    private Task task;
}
