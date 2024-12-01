package com.maemresen.server.manager.cli.utils;

import java.time.Duration;
import lombok.experimental.UtilityClass;

/** Utility class for introducing delays in program execution. */
@UtilityClass
public class ExecutionPauseUtils {

  /**
   * Pauses execution for the specified duration.
   *
   * @param duration the duration to pause, as a {@link Duration} object
   * @throws InterruptedException if the thread is interrupted while sleeping
   */
  public static void delay(Duration duration) throws InterruptedException {
    Thread.sleep(duration.toMillis());
  }
}
