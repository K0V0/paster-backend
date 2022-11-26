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

    public static PlatformEnum getByName(String platform) {
        if (platform == null || platform.trim().length() == 0) return UNKNOWN;
        return Arrays.stream(PlatformEnum.values())
                .filter(val -> val.name().equals(platform.trim().toUpperCase()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
