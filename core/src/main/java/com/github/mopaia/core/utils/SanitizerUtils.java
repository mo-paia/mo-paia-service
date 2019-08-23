package com.github.mopaia.core.utils;

import java.util.Optional;

public final class SanitizerUtils {

    private SanitizerUtils() {
    }


    public static String sanitize(String dirty) {
        return Optional.ofNullable(dirty)
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .orElse(null);
    }
}
