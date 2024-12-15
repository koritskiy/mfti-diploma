package com.koritski.teamsync.backend.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FillBankInfoRq {
    private String fullOrganizationTitle;
    private String inn;
    private String kpp;
    private String accountNumber;
    private String organizationBank;
    private String bankBik;
    private String bankAccountNumber;
}
