package nl.muttalipkucuk.workinghours.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;

public class TimeUtils {                                                                            // TODO: rename class with a better one
    private static final String DEFAULT_ZONE_ID = "Europe/Amsterdam";                               // TODO: internationalization

    public static Function0<LocalDateTime> getCurrentDateTime = LocalDateTime::now;

    public static Function1<LocalDateTime, ZonedDateTime> convertByTimeZone = (dateTime) -> {
        ZoneId zoneId = ZoneId.of(DEFAULT_ZONE_ID);
        return dateTime.atZone(zoneId);
    };

    public static Function1<ZonedDateTime, Tuple2<Integer, Integer>> getHourAndMinute = (dateTime) -> {
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        return Tuple.of(hour, minute);
    };




}
