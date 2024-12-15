package com.koritski.teamsync.backend.entity.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Worker;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "worker_function", schema = "tracker")
@Data
@Accessors(chain = true)
public class WorkerFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tariff_rate")
    private Long tariffRate;

    @Column(name = "function_name")
    private String functionName;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    @JsonIgnore
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "function_id")
    @JsonIgnore
    private Function function;
}


