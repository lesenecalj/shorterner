package com.shorterner.shorterner.utils;

public final class Helper {

    public static Boolean validLongUrl(String url) {
        String u = url.trim();
        return u.startsWith("http://") || u.startsWith("https://");
    }

}
