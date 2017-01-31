package org.telegram.timertasks;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateUtils {

    public static long getDurationInMillis(LocalDateTime from, LocalDateTime to) {
        return getInMillis(to) - getInMillis(from);
    }

    private static long getInMillis(LocalDateTime time) {
        return time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }
}
