package com.koritski.teamsync.auth.dto.rq;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RqFillTelegramData {
    private String firstName;
    private String lastName;
    private String chatId;
    private String username;
}
