package nl.muttalipkucuk.workinghours.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import io.vavr.Tuple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TimeUtilsTest {

    @Test
    public void shouldConvertToString() {
        String timeToString = TimeUtils.convertDateTimeToString.apply(LocalDateTime.parse("2019-10-25T01:02:03"));
        assertThat(timeToString, is("01:02:03"));
    }

    @Test
    public void shouldGetTextForWorkingAsExpected() {
        String text = TimeUtils.getSummaryText.apply("09:00:00", "17:30:00");
        assertThat(text, is("Worked: 8 hours and 30 minutes.\n"));
    }

    @Test
    public void shouldGetTextForWorkingExtra() {
        String text = TimeUtils.getSummaryText.apply("09:00:00", "18:00:00");
        assertThat(text, is("Worked: 9 hours.\nWorked extra: 30 minutes.\n"));
    }

    @Test
    public void shouldGetTextForWorkingTooLess() {
        String text = TimeUtils.getSummaryText.apply("09:00:00", "17:00:00");
        assertThat(text, is("Worked: 8 hours.\nNot worked: 30 minutes.\n"));
    }

    @Test
    public void shouldContainTimeUnitInPlural() {
        String formattedTime = TimeUtils.formatHourAndMinute.apply(Tuple.of(2L, 2L));
        assertThat(formattedTime, is("2 hours and 2 minutes"));
    }

    @Test
    public void shouldContainTimeUnitInSingular() {
        String formattedTime = TimeUtils.formatHourAndMinute.apply(Tuple.of(1L, 1L));
        assertThat(formattedTime, is("1 hour and 1 minute"));
    }

    @Test
    public void shouldContainHourOnly() {
        String formattedTime = TimeUtils.formatHourAndMinute.apply(Tuple.of(5L, 0L));
        assertThat(formattedTime, is("5 hours"));
    }

    @Test
    public void shouldContainMinuteOnly() {
        String formattedTime = TimeUtils.formatHourAndMinute.apply(Tuple.of(0L, 5L));
        assertThat(formattedTime, is("5 minutes"));
    }
}
