package com.maemresen.server.manager.cli.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

/**
 * Utility class for working with {@link Duration}, {@link LocalDate}, and {@link LocalDateTime}
 * objects. Provides methods for parsing and formatting date and time objects.
 */
@UtilityClass
public class DateTimeUtils {

  private static final DateTimeFormatter DATE_PARAMETER_PATTERN =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_TIME_PARAMETER_PATTERN =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_TIME_WITH_SECONDS_PATTERN =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

  /**
   * Formats a {@link LocalDateTime} into a string using the format "yyyy-MM-dd HH:mm".
   *
   * @param creationTime the {@link LocalDateTime} to format
   * @return the formatted date-time string
   * @throws NullPointerException if the creationTime is null
   */
  public static String formatDateTimeWithSeconds(LocalDateTime creationTime) {
    return creationTime.format(DATE_TIME_WITH_SECONDS_PATTERN);
  }

  /**
   * Parses a string into a {@link LocalDate} using the format "yyyy-MM-dd".
   *
   * @param dateString the date string to parse
   * @return the parsed {@link LocalDate} object
   * @throws NullPointerException if the dateString is null
   * @throws java.time.format.DateTimeParseException if the dateString cannot be parsed
   */
  public static LocalDate parseDate(String dateString) {
    return LocalDate.parse(dateString, DATE_PARAMETER_PATTERN);
  }

  /**
   * Parses a string into a {@link LocalDateTime} using the format "yyyy-MM-dd HH:mm".
   *
   * @param dateString the date-time string to parse
   * @return the parsed {@link LocalDateTime} object
   * @throws NullPointerException if the dateString is null
   * @throws java.time.format.DateTimeParseException if the dateString cannot be parsed
   */
  public static LocalDateTime parseDateTimeWithoutSeconds(String dateString) {
    return LocalDateTime.parse(dateString, DATE_TIME_PARAMETER_PATTERN);
  }

  /**
   * Returns the current date and time based on the system's default time zone.
   *
   * @return the current {@link LocalDateTime} using the system's default {@link ZoneId}.
   */
  public static LocalDateTime now() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }
}
