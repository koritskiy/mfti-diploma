package com.koritski.teamsync.backend.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentDocNotifyRs {
    private String totalCash;
    private String userTgChatId;
}
