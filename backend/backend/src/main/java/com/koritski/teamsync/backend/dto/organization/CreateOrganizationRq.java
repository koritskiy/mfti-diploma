package com.koritski.teamsync.backend.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors (chain = true)
public class CreateOrganizationRq {
    private String name;
    private String address;
    private String link;
}
