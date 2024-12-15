package com.koritski.teamsync.backend.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrganizationWorkersStatRs {
    private List<EachWorker> workers;

    @Data
    @Accessors(chain = true)
    public static class EachWorker {
        private String name;
        private String surname;
        private String email;
        private String totalCash;
        private String totalTaskOnWorker;
    }
}
