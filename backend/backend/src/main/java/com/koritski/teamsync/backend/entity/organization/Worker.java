package com.koritski.teamsync.backend.entity.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.worker.WorkTime;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "worker", schema = "tracker")
@Data
@Accessors(chain = true)
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private String status;

    @Column(name = "inn")
    private String inn;

    @Column(name = "worker_bank")
    private String workerBank;

    @Column(name = "bank_bik")
    private String bankBik;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "is_organization_admin")
    private Boolean isOrganizationAdmin;

    @JsonIgnore
    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.PERSIST)
    private List<WorkerFunction> workerFunctions;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Task> workerTasks;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.PERSIST)
    private List<WorkTime> workTimeList;
}

