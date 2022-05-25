package com.kovospace.paster.item.dtos;

import java.util.Arrays;

public enum PlatformEnum {
    WEBAPP,
    MOBILE_ANDROID,
    MOBILE_APPLE,
    DESKTOP_WINDOWS,
    DESKTOP_LINUX,
    DESKTOP_APPLE,
    UNKNOWN;

    public static PlatformEnum getByName(String name) {
        if (name == null || name.trim().equals("")) {
            return UNKNOWN;
        }
        return Arrays.stream(PlatformEnum.values())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
