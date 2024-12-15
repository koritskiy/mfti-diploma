package com.koritski.teamsync.backend.entity.organization;

import com.koritski.teamsync.backend.entity.task.Tag;
import com.koritski.teamsync.backend.entity.task.TaskBoard;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "organization", schema = "tracker")
@Data
@Accessors(chain = true)
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "link")
    private String link;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "full_organization_title")
    private String fullOrganizationTitle;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "organization_bank")
    private String organizationBank;

    @Column(name = "bank_bik")
    private String bankBik;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST)
    private List<Worker> workers;

    @OneToMany(mappedBy = "organization")
    private List<TaskBoard> taskBoards;

    @OneToMany(mappedBy = "organization")
    private List<Function> functions;

    @OneToMany(mappedBy = "organization")
    private List<Tag> tags;
}
