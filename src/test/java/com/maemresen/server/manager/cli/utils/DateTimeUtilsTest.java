package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  @Nested
  class FormatDurationTests {

    @Test
    void shouldReturnFormattedStringForValidDuration() {
      Duration duration = Duration.ofHours(1).plusMinutes(2).plusSeconds(3);

      String formatted = DateTimeUtils.formatDuration(duration);

      assertThat(formatted)
          .as("The formatted duration should match the expected format hh:mm:ss")
          .isEqualTo("01:02:03");
    }

    @Test
    void shouldHandleZeroDuration() {
      Duration duration = Duration.ZERO;

      String formatted = DateTimeUtils.formatDuration(duration);

      assertThat(formatted)
          .as("The formatted duration for zero should be 00:00:00")
          .isEqualTo("00:00:00");
    }

    @Test
    void shouldThrowExceptionForNullDuration() {
      assertThatThrownBy(() -> DateTimeUtils.formatDuration(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("Duration cannot be null");
    }
  }
}
