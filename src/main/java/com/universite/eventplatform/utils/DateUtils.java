package com.universite.eventplatform.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String format(LocalDateTime dt) {
        return dt == null ? "" : dt.format(DISPLAY_FORMAT);
    }

    public static boolean isUpcoming(LocalDateTime dt) {
        return dt != null && dt.isAfter(LocalDateTime.now());
    }

    public static boolean isPast(LocalDateTime dt) {
        return dt != null && dt.isBefore(LocalDateTime.now());
    }

    public static boolean isOngoing(LocalDateTime start, int durationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(start) && now.isBefore(start.plusMinutes(durationMinutes));
    }
}