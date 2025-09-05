package com.shorterner.shorterner.utils;

public class CodeGen {
    public static String codeFor(String raw) {
        String t = raw == null ? "" : raw.trim();
        byte[] sha = sha256(t);
        return base62(sha, 6);
    }

    private static byte[] sha256(String s) {
        try {
            return java.security.MessageDigest.getInstance("SHA-256")
                    .digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String base62(byte[] bytes, int firstN) {
        java.math.BigInteger n = new java.math.BigInteger(1, java.util.Arrays.copyOf(bytes, firstN));
        var base = java.math.BigInteger.valueOf(62);
        char[] A = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        if (n.signum() == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (n.signum() > 0) {
            int rem = n.mod(base).intValue();
            sb.append(A[rem]);
            n = n.divide(base);
        }
        return sb.reverse().toString();
    }
}