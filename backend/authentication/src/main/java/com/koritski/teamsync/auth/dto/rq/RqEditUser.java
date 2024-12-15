package com.koritski.teamsync.auth.dto.rq;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RqEditUser {
    private Long userId;
    private String firstName;
    private String lastName;
    private String password;
}
