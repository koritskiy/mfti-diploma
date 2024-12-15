package com.koritski.teamsync.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER(1L, "USER"),
    ADMIN(2L, "ADMIN"),
    MODERATOR(3L, "MODERATOR");

    private final Long id;
    private final String name;

    private static final Map<Long, String> eRole = Arrays
            .stream(RoleEnum.values())
            .collect(toMap(RoleEnum::getId, RoleEnum::getName));

    public static String getNameById(Long id) {
        return eRole.getOrDefault(id, null);
    }
}
