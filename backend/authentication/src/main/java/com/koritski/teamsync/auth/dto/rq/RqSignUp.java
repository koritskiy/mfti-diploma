package com.koritski.teamsync.auth.dto.rq;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RqSignUp {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
