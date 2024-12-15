package com.koritski.teamsync.auth.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RsTgUserData {
    private String tgChatId;
    private String tgUsername;
}
