package com.maemresen.server.manager.cli.utils;

import java.util.Optional;
import lombok.experimental.UtilityClass;

/**
 * Utility class for retrieving environment variables with optional default values.
 *
 * <p>This class provides a convenient way to fetch environment variables from the system. If the
 * specified variable is not set, a default value can be returned instead.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * String value = EnvUtils.getEnv("MY_ENV_VAR", "default");
 * }</pre>
 */
@UtilityClass
public class EnvUtils {

  /**
   * Retrieves the value of an environment variable. If the variable is not set, returns the
   * provided default value.
   *
   * @param key the name of the environment variable to retrieve
   * @param defaultValue the default value to return if the environment variable is not set
   * @return the value of the environment variable if set; otherwise, the default value
   */
  public static String getEnv(String key, String defaultValue) {
    return Optional.ofNullable(System.getenv(key)).orElse(defaultValue);
  }
}
