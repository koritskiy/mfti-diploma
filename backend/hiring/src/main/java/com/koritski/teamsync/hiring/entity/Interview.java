package com.koritski.teamsync.hiring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "interview", schema = "hiring")
@Data
@Accessors(chain = true)
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recruiter")
    private String recruiter;

    @Column(name = "interview_feedback")
    private String interviewFeedback;

    @Column(name = "interview_decision")
    private String interviewDecision;

    @JsonIgnore
    @JoinColumn(name = "candidate_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.PERSIST)
    private List<InterviewTaskStack> interviewTaskStack;
}
