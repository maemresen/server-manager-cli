package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  @Nested
  class FormatDurationTests {

    @Test
    void shouldFormatDurationForValidDuration() {
      Duration duration = Duration.ofHours(2).plusMinutes(30).plusSeconds(15);
      String formattedDuration = DateTimeUtils.formatDuration(duration);

      assertThat(formattedDuration).isEqualTo("02:30:15");
    }

    @Test
    void shouldFormatDurationForZeroDuration() {
      Duration duration = Duration.ZERO;
      String formattedDuration = DateTimeUtils.formatDuration(duration);

      assertThat(formattedDuration).isEqualTo("00:00:00");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDurationIsNull() {
      assertThatThrownBy(() -> DateTimeUtils.formatDuration(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("Duration cannot be null");
    }
  }

  @Nested
  class FormatDateTimeWithSecondsTests {

    @Test
    void shouldFormatDateTimeWithSecondsForValidDateTime() {
      LocalDateTime dateTime = LocalDateTime.of(2024, 12, 2, 15, 45, 30);
      String formattedDateTime = DateTimeUtils.formatDateTimeWithSeconds(dateTime);

      assertThat(formattedDateTime).isEqualTo("2024-12-02 15:45:30");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDateTimeIsNull() {
      assertThatThrownBy(() -> DateTimeUtils.formatDateTimeWithSeconds(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  class ParseDateTests {

    @Test
    void shouldParseValidDateString() {
      String dateString = "2024-12-02";
      LocalDate parsedDate = DateTimeUtils.parseDate(dateString);

      assertThat(parsedDate).isEqualTo(LocalDate.of(2024, 12, 2));
    }

    @Test
    void shouldThrowDateTimeParseExceptionForInvalidDateString() {
      String invalidDateString = "invalid-date";

      assertThatThrownBy(() -> DateTimeUtils.parseDate(invalidDateString))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("Text 'invalid-date' could not be parsed");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDateStringIsNull() {
      assertThatThrownBy(() -> DateTimeUtils.parseDate(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  class ParseDateTimeWithoutSecondsTests {

    @Test
    void shouldParseValidDateTimeStringWithoutSeconds() {
      String dateTimeString = "2024-12-02 15:45";
      LocalDateTime parsedDateTime = DateTimeUtils.parseDateTimeWithoutSeconds(dateTimeString);

      assertThat(parsedDateTime).isEqualTo(LocalDateTime.of(2024, 12, 2, 15, 45));
    }

    @Test
    void shouldThrowDateTimeParseExceptionForInvalidDateTimeString() {
      String invalidDateTimeString = "invalid-datetime";

      assertThatThrownBy(() -> DateTimeUtils.parseDateTimeWithoutSeconds(invalidDateTimeString))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("Text 'invalid-datetime' could not be parsed");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDateTimeStringIsNull() {
      assertThatThrownBy(() -> DateTimeUtils.parseDateTimeWithoutSeconds(null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  class NowTests {

    @Test
    void testNow() {
      LocalDateTime before = LocalDateTime.now(ZoneId.systemDefault());

      LocalDateTime result = DateTimeUtils.now();

      LocalDateTime after = LocalDateTime.now(ZoneId.systemDefault());

      assertTrue(
          !result.isBefore(before) && !result.isAfter(after),
          "The result should be between 'before' and 'after' timestamps");
    }
  }
}
