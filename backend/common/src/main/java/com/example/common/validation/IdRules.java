package com.example.common.validation;

import java.util.regex.Pattern;

public final class IdRules {
    private static final Pattern DIGITS = Pattern.compile("^\\d+$");
    private static final long USER_MIN = 100000L;
    private static final long USER_MAX = 999999999999L;
    private static final int ORDER_LEN = 18;

    private IdRules() {
    }

    public static boolean validUserId(String userId) {
        if (userId == null || !DIGITS.matcher(userId).matches()) {
            return false;
        }
        long id = Long.parseLong(userId);
        return id >= USER_MIN && id <= USER_MAX;
    }

    public static String buildOrderId(long userId, long sequence, String travelDateYYYYMMDD) {
        String base = String.format("%s%05d%04d", travelDateYYYYMMDD, userId % 100000, sequence % 10000);
        String checksum = String.valueOf(luhn(base));
        String candidate = base + checksum;
        if (candidate.length() >= ORDER_LEN) {
            return candidate.substring(0, ORDER_LEN);
        }
        return "0".repeat(ORDER_LEN - candidate.length()) + candidate;
    }

    public static boolean validOrderId(String orderId) {
        if (orderId == null || orderId.length() != ORDER_LEN || !DIGITS.matcher(orderId).matches()) {
            return false;
        }
        String base = orderId.substring(0, ORDER_LEN - 1);
        int check = orderId.charAt(ORDER_LEN - 1) - '0';
        return luhn(base) == check;
    }

    private static int luhn(String input) {
        int sum = 0;
        boolean doubleIt = true;
        for (int i = input.length() - 1; i >= 0; i--) {
            int digit = input.charAt(i) - '0';
            if (doubleIt) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleIt = !doubleIt;
        }
        return (10 - (sum % 10)) % 10;
    }
}
