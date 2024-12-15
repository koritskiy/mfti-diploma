package com.koritski.teamsync.backend.dto.rs;

import com.koritski.teamsync.backend.entity.organization.Organization;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetMyOrganizationsRs {
    private Long count;
    private List<Organization> organizations;
}
