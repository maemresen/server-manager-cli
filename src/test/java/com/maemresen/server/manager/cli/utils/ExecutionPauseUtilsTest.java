package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ExecutionPauseUtilsTest {

  @Nested
  class DelayTests {

    @Test
    void shouldPauseForSpecifiedDuration() throws InterruptedException {
      Duration delayDuration = Duration.ofMillis(100);

      long startTime = System.currentTimeMillis();
      ExecutionPauseUtils.delay(delayDuration);
      long endTime = System.currentTimeMillis();

      assertThat(endTime - startTime)
          .isGreaterThanOrEqualTo(delayDuration.toMillis())
          .isLessThan(
              delayDuration.toMillis() + 50); // Allow a small buffer for timing inaccuracies
    }

    @Test
    void shouldThrowInterruptedExceptionWhenThreadIsInterrupted() {
      Duration delayDuration = Duration.ofMillis(100);
      Thread currentThread = Thread.currentThread();

      Thread interrupter =
          new Thread(
              () -> {
                try {
                  Thread.sleep(50);
                  currentThread.interrupt();
                } catch (InterruptedException ignored) {
                  // empty
                }
              });
      interrupter.start();

      assertThatThrownBy(() -> ExecutionPauseUtils.delay(delayDuration))
          .isInstanceOf(InterruptedException.class);
    }
  }
}
