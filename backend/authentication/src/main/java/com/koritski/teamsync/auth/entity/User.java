package com.koritski.teamsync.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Accessors(chain = true)
@Table(schema = "auth", name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "tg_first_name")
    private String tgFirstName;

    @Column(name = "tg_last_name")
    private String tgLastName;

    @Column(name = "tg_chat_id")
    private String tgChatId;

    @Column(name = "tg_username")
    private String tgUsername;

    @Column(name = "github_token")
    private String gitHubToken;

    @Column(name = "secret_code")
    private String secretCode;

    @Column(name = "is_email_approved")
    private Boolean isEmailApproved;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RoleUser> userRoles = new HashSet<RoleUser>();
}
