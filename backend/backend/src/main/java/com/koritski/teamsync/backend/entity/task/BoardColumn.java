package com.koritski.teamsync.backend.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "board_column", schema = "tracker")
@Data
@Accessors(chain = true)
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private TaskBoard board;

    @OneToMany(mappedBy = "boardColumn")
    private List<Task> tasks;
}