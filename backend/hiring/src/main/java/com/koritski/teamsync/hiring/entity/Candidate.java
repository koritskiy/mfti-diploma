package com.koritski.teamsync.hiring.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "candidate", schema = "hiring")
@Data
@Accessors(chain = true)
public class Candidate {
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

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "resume_link")
    private String resumeLink;

    @Column(name = "position")
    private String position;

    @Column(name = "tariff")
    private String tariff;

    @Column(name = "phone")
    private String phone;

    @Column(name = "telegram_name")
    private String telegramName;

    @Column(name = "is_hired")
    private Boolean isHired;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.PERSIST)
    private List<Interview> interviews;
}
