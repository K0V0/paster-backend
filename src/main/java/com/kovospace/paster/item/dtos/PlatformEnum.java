package com.kovospace.paster.item.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Arrays;

// springDoc
//@Schema(enumAsRef = true, ref = "#/components/schemas/PlatformEnum")
//@Schema(enumAsRef = true)


public enum PlatformEnum implements Serializable {
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
