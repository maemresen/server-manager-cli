package com.maemresen.server.manager.cli.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

/** Utility class for working with {@link Duration} and {@link LocalDateTime} objects. */
@UtilityClass
public class DateTimeUtils {

  /**
   * Formats a {@link Duration} into a string in the format "hh:mm:ss".
   *
   * @param duration the {@link Duration} to format
   * @return the formatted duration as a string in "hh:mm:ss" format
   * @throws NullPointerException if the duration is null
   */
  public static String formatDuration(Duration duration) {
    if (duration == null) {
      throw new NullPointerException("Duration cannot be null");
    }
    long hours = duration.toHours();
    long minutes = duration.toMinutesPart();
    long seconds = duration.toSecondsPart();

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }
}
