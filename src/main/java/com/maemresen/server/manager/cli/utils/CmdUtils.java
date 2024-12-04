package com.maemresen.server.manager.cli.utils;

import com.maemresen.server.manager.cli.exception.EnumValueParseException;
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
   * Retrieves an enum parameter from the specified {@link CommandLine} instance.
   *
   * <p>This method attempts to extract the value of a parameter from the provided {@link
   * CommandLine}, map it to an enum constant of the specified type, and return it as an {@link
   * Optional}. If the parameter does not exist, {@link Optional#empty()} is returned. If the value
   * cannot be mapped to a valid enum constant, an {@link EnumValueParseException} is thrown.
   *
   * @param cmd the {@link CommandLine} object containing the parameters
   * @param parameter the name of the parameter to extract
   * @param enumClass the {@link Class} object of the enum type to map the parameter to
   * @param <E> the type of the enum
   * @return an {@link Optional} containing the corresponding enum value if the parameter exists and
   *     is valid; otherwise, {@link Optional#empty()}
   * @throws EnumValueParseException if the parameter value cannot be mapped to the specified enum
   *     type
   */
  public static <E extends Enum<E>> Optional<E> getEnumParameter(
      CommandLine cmd, String parameter, Class<E> enumClass) throws EnumValueParseException {

    Optional<String> enumValueString = getParameter(cmd, parameter, Function.identity());
    if (enumValueString.isEmpty()) {
      return Optional.empty();
    }

    try {
      return Optional.of(Enum.valueOf(enumClass, enumValueString.get().toUpperCase()));
    } catch (IllegalArgumentException exception) {
      throw new EnumValueParseException(parameter);
    }
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
