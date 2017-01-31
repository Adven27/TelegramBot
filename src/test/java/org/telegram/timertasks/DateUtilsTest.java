package org.telegram.timertasks;

import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.telegram.timertasks.DateUtils.getDurationInMillis;

public class DateUtilsTest {

    private static final long ONE_MILLISECOND = MILLISECONDS.convert(1, MILLISECONDS);

    @Test
    public void shouldComputeDurationInMillis() {
        LocalDateTime from = now();
        LocalDateTime to = from.plus(1, MILLIS);
        assertEquals(ONE_MILLISECOND, getDurationInMillis(from, to));
    }
    
}