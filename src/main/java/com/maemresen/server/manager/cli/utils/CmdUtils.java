package com.maemresen.server.manager.cli.utils;

import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;

/** Utility class for handling and extracting command-line parameters using Apache Commons CLI. */
@UtilityClass
public class CmdUtils {

  /**
   * Retrieves a boolean parameter from the given {@link CommandLine}.
   *
   * @param cmd the {@link CommandLine} object to extract the parameter from
   * @param parameter the name of the parameter to extract
   * @return an {@link Optional} containing {@code true} if the parameter exists; otherwise, {@code
   *     Optional.empty()}
   */
  public static Optional<Boolean> getBooleanParameter(CommandLine cmd, String parameter) {
    return getParameter(cmd, parameter, value -> true);
  }

  /**
   * Retrieves an enum parameter from the given {@link CommandLine}.
   *
   * @param cmd the {@link CommandLine} object to extract the parameter from
   * @param parameter the name of the parameter to extract
   * @param enumClass the class of the enum to map the parameter to
   * @param <E> the type of the enum
   * @return an {@link Optional} containing the mapped enum value if the parameter exists and can be
   *     mapped; otherwise, {@code Optional.empty()}
   * @throws IllegalArgumentException if the parameter value cannot be mapped to the specified enum
   */
  public static <E extends Enum<E>> Optional<E> getEnumParameter(
      CommandLine cmd, String parameter, Class<E> enumClass) {
    return getParameter(cmd, parameter, value -> Enum.valueOf(enumClass, value.toUpperCase()));
  }

  /**
   * Retrieves a parameter from the given {@link CommandLine} and maps it using the provided
   * function.
   *
   * @param cmd the {@link CommandLine} object to extract the parameter from
   * @param parameter the name of the parameter to extract
   * @param mapper the function to map the parameter value
   * @param <T> the type of the mapped parameter
   * @return an {@link Optional} containing the mapped value if the parameter exists; otherwise,
   *     {@code Optional.empty()}
   */
  public static <T> Optional<T> getParameter(
      CommandLine cmd, String parameter, Function<String, T> mapper) {
    if (cmd.hasOption(parameter)) {
      String optionValue = cmd.getOptionValue(parameter);
      return Optional.of(mapper.apply(optionValue));
    }
    return Optional.empty();
  }
}
