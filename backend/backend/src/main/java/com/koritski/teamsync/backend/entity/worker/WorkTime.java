package com.koritski.teamsync.backend.entity.worker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Worker;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "work_time", schema = "tracker")
@Data
@Accessors(chain = true)
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    // Сумма, которую сотрудник заработал от start до end по тарифу.
    // Проставляется после окончания отрезка времени
    @Column(name = "total")
    private Long total;

    @Column(name = "tariff_rate")
    private Long tariffRate;

    @Column(name = "is_payment_loaded")
    private Boolean isPaymentLoaded = false;

    @JoinColumn(name = "worker_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Worker worker;

    @JoinColumn(name = "function_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Function function;
}
