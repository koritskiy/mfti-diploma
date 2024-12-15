package com.koritski.teamsync.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkerStatusEnum {
    WORK("WORK", "На данный момент сотрудник работает в организации."),
    FIRED("FIRED", "Сотрудник больше не работает в организации.");

    private final String name;
    private final String description;
}
