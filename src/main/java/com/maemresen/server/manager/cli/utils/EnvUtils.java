package com.maemresen.server.manager.cli.utils;

import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtils {

  public static String getEnv(String key, String defaultValue) {
    return Optional.ofNullable(System.getenv(key)).orElse(defaultValue);
  }
}
