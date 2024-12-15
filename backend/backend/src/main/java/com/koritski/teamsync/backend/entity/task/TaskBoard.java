package com.koritski.teamsync.backend.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.organization.Organization;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task_board", schema = "tracker")
@Data
@Accessors(chain = true)
public class TaskBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @OneToMany(mappedBy = "board")
    private List<BoardColumn> boardColumns;
}
