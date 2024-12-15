package com.koritski.teamsync.auth.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RsAuthUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String token;
    private Boolean isUserAdmin;
    private String gitHubToken;
}
