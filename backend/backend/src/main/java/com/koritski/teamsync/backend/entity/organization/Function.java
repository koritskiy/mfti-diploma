package com.koritski.teamsync.backend.entity.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.worker.WorkTime;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "function", schema = "tracker")
@Data
@Accessors(chain = true)
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @OneToMany(mappedBy = "function", cascade = CascadeType.PERSIST)
    private List<WorkerFunction> workerFunctions;

    @JsonIgnore
    @OneToMany(mappedBy = "function", cascade = CascadeType.PERSIST)
    private List<WorkTime> workTimeList;
}