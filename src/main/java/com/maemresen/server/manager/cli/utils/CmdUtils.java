package com.maemresen.server.manager.cli.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;

@UtilityClass
public class CmdUtils {
  private static final DateTimeFormatter DATE_TIME_PATTERN =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static Optional<LocalDateTime> getDateTimeOption(CommandLine cmd, String option) {
    return getOption(cmd, option, value -> LocalDateTime.parse(value, DATE_TIME_PATTERN));
  }

  public static Optional<LocalDate> getDateOption(CommandLine cmd, String option) {
    return getOption(cmd, option, value -> LocalDate.parse(value, DATE_PATTERN));
  }

  public static Optional<String> getStringOption(CommandLine cmd, String option) {
    return getOption(cmd, option, Function.identity());
  }

  public static <E extends Enum<E>> Optional<E> getEnumOption(
      CommandLine cmd, String option, Class<E> enumClass) {
    return getOption(cmd, option, value -> Enum.valueOf(enumClass, value.toUpperCase()));
  }

  public static <T> Optional<T> getOption(
      CommandLine cmd, String option, Function<String, T> mapper) {
    if (cmd.hasOption(option)) {
      String optionValue = cmd.getOptionValue(option);
      return Optional.of(mapper.apply(optionValue));
    }
    return Optional.empty();
  }
}
