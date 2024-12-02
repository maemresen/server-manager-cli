package com.maemresen.server.manager.cli.utils;

import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;

@UtilityClass
public class CmdUtils {
  public static Optional<Boolean> getBooleanParameter(CommandLine cmd, String parameter) {
    return getParameter(cmd, parameter, value -> true);
  }

  public static <E extends Enum<E>> Optional<E> getEnumParameter(
      CommandLine cmd, String parameter, Class<E> enumClass) {
    return getParameter(cmd, parameter, value -> Enum.valueOf(enumClass, value.toUpperCase()));
  }

  public static <T> Optional<T> getParameter(
      CommandLine cmd, String parameter, Function<String, T> mapper) {
    if (cmd.hasOption(parameter)) {
      String optionValue = cmd.getOptionValue(parameter);
      return Optional.of(mapper.apply(optionValue));
    }
    return Optional.empty();
  }
}
