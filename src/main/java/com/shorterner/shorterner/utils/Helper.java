package com.shorterner.shorterner.utils;

public final class Helper {

    public static String canonicalize(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        String t = raw.trim();

        if (!t.startsWith("http://") && !t.startsWith("https://")) {
            t = "https://" + t;
        }

        return t.toLowerCase();
    }

}
