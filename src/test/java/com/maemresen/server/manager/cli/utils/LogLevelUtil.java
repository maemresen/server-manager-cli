package com.maemresen.server.manager.cli.utils;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.slf4j.event.Level;

@UtilityClass
public class LogLevelUtil {

  public static Level logbackToSlf4j(ch.qos.logback.classic.Level level) {
    return Level.valueOf(level.levelStr);
  }

  public static ch.qos.logback.classic.Level slf4jToLogback(Level level) {
    return ch.qos.logback.classic.Level.toLevel(level.name());
  }

  public static boolean isSame(Level slf4jLevel, ch.qos.logback.classic.Level logbackLevel) {
    return Objects.equals(slf4jLevel.name(), logbackLevel.levelStr);
  }

  public static boolean isSame(ch.qos.logback.classic.Level logbackLevel, Level slf4jLevel) {
    return isSame(slf4jLevel, logbackLevel);
  }
}
