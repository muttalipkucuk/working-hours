package nl.muttalipkucuk.workinghours.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class TimeUtils {                                                                            // TODO: rename class with a better one
    private static final String DEFAULT_ZONE_ID = "Europe/Amsterdam";                               // TODO: internationalization
    private static final Long WORKING_DAY_IN_MINUTES_LUNCH_INCLUDED = 510L;                         // TODO: define as 8.5 (in env var)
    private static final Integer MINUTES_IN_OUR_HOUR = 60;                                          // TODO: use an existing Java library
    private static final String TEXT_WORKED = "Worked: %s.\n";
    private static final String TEXT_WORKED_EXTRA = "Worked extra: %s.\n";
    private static final String TEXT_NOT_WORKED = "Not worked: %s.\n";

    public static Function0<String> getCurrentTime = () ->
            TimeUtils.getDateTimeNow
                    .andThen(TimeUtils.convertDateTimeToString).apply();

    public static Function2<String, String, String> getSummaryText = (startTime, stopTime) ->
            TimeUtils.calculateWorkedTime
                    .andThen(TimeUtils.createSummaryText)
                    .apply(startTime, stopTime);

    private static Function2<String, String, Tuple2<Long, Long>> calculateWorkedTime = (startTime, stopTime) -> {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime stop = LocalTime.parse(stopTime);

        Long timeWorked = Duration.between(start, stop).toMinutes();
        Long timeWorkedExtra = TimeUtils.getTimeWorkedExtra.apply(timeWorked);
        return Tuple.of(timeWorked, timeWorkedExtra);
    };

    private static Function1<Long, Long> getTimeWorkedExtra = timeWorked ->
            Math.subtractExact(timeWorked, WORKING_DAY_IN_MINUTES_LUNCH_INCLUDED);

    private static Function1<Tuple2<Long, Long>, String> createSummaryText = (workedAndWorkedExtraTime) -> {
        Long worked = workedAndWorkedExtraTime._1;
        Long workedExtra = workedAndWorkedExtraTime._2;
        Long workingDiff = worked - WORKING_DAY_IN_MINUTES_LUNCH_INCLUDED;

        String textWorked = TimeUtils.createText.apply(TEXT_WORKED, worked);
        Option<String> textWorkedExtra = Option.when(workingDiff > 0L, () -> TimeUtils.createText.apply(TEXT_WORKED_EXTRA, workedExtra));
        Option<String> textNotWorked = Option.when(workingDiff < 0L, () -> TimeUtils.createText.apply(TEXT_NOT_WORKED, Math.abs(workedExtra)));

        return textWorked + textWorkedExtra.getOrElse("") + textNotWorked.getOrElse("");
    };

    private static Function2<String, Long, String> createText = (textTemplate, timeInMinutes) -> {
        String timeAsText = TimeUtils.splitToHoursAndMinutes
                .andThen(TimeUtils.formatHourAndMinute)
                .apply(timeInMinutes);

        return String.format(textTemplate, timeAsText);
    };

    static Function1<Tuple2<Long, Long>, String> formatHourAndMinute = (hourAndMinute) -> {
        Long hour = hourAndMinute._1;
        Long minute = hourAndMinute._2;
        Function2<Long, String, String> formatTimeToText = (time, timeUnit) ->
                String.format("%d %s", time, time > 1L ? timeUnit + "s" : timeUnit);

        Option<String> textHour = Option.when(hour != 0L, hour).map(x -> formatTimeToText.apply(x, "hour"));
        Option<String> textMinute = Option.when(minute != 0L, minute).map(x -> formatTimeToText.apply(x, "minute"));
        return List.of(textHour, textMinute)
                .filter(Option::isDefined)
                .map(x -> x.getOrElse(""))
                .mkString(" and ");
    };

    private static Function1<Long, Tuple2<Long, Long>> splitToHoursAndMinutes = (minutes) ->
            Tuple.of(minutes / MINUTES_IN_OUR_HOUR, minutes % MINUTES_IN_OUR_HOUR);

    private static Function0<LocalDateTime> getDateTimeNow = LocalDateTime::now;

    static Function1<LocalDateTime, String> convertDateTimeToString = (dateTime) ->
            TimeUtils.applyTimeZone
                    .andThen(TimeUtils.getTime)
                    .andThen(TimeUtils.convertToString)
                    .apply(dateTime);

    private static Function1<LocalDateTime, ZonedDateTime> applyTimeZone =
            dateTime -> dateTime.atZone(ZoneId.of(DEFAULT_ZONE_ID));


    private static Function1<ZonedDateTime, Tuple3<Integer, Integer, Integer>> getTime = dateTime -> {
        Integer hour = dateTime.getHour();
        Integer minute = dateTime.getMinute();
        Integer second = dateTime.getSecond();
        return Tuple.of(hour, minute, second);
    };

    private static Function1<Tuple3<Integer, Integer, Integer>, String> convertToString = (hourMinuteSecond) ->
            String.format("%02d:%02d:%02d", hourMinuteSecond._1, hourMinuteSecond._2, hourMinuteSecond._3);

}
