package com.koritski.teamsync.hiring.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCandidateRq {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long organizationId;
    private String resumeLink;
    private String position;
    private String tariff;
    private String phone;
    private String telegramName;
}
