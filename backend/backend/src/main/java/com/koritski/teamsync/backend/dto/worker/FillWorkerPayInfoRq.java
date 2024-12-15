package com.koritski.teamsync.backend.dto.worker;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FillWorkerPayInfoRq {
    private String middleName;
    private String inn;
    private String workerBank;
    private String bankBik;
    private String bankAccountNumber;
    private String accountNumber;
}
